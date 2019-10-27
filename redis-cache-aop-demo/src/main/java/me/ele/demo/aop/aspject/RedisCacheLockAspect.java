package me.ele.demo.aop.aspject;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ele.demo.aop.annotition.CacheAuto;
import me.ele.demo.aop.annotition.CacheType;
import me.ele.demo.aop.annotition.LockAuto;
import me.ele.demo.utils.AspectSupportUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * 缓存和分布式锁切面，
 * CacheAuto的注解的切面
 * LockAuto的注解的切面
 *
 * @author daiderong
 * @see CacheAuto {@link #cacheAuto(ProceedingJoinPoint)}
 * @see LockAuto {@link #lockAuto(ProceedingJoinPoint)}
 * @since 3.1.1
 */
@Slf4j
@Aspect
public abstract class RedisCacheLockAspect {

    /**
     * redisConnectionFactory，主要用于生成redisTemplate
     */
    @Getter
    protected RedisConnectionFactory redisConnectionFactory;

    /**
     * redisTemplateCache 用于生成缓存数据，会在设置redisConnectionFactory时生成
     *
     * @see #initDefaultRedisTemplate
     */
    @Getter
    protected RedisTemplate redisTemplateCache;

    /**
     * redisTemplateLock 用于设置分布式锁，会在设置redisConnectionFactory时生成
     *
     * @see #initDefaultRedisTemplate
     */
    @Getter
    protected RedisTemplate redisTemplateLock;

    /**
     * 是否支持null缓存，默认如果为null则不设置
     * 当设置为true时，返回值为null也会缓存到redis中
     */
    @Setter
    @Getter
    protected boolean cacheNullAble = false;

    /**
     * cache的过期时间，单位为秒
     * 大于0 表示设置秒数
     * 设置-1表示无限时间
     */
    @Setter
    @Getter
    protected int cacheTimeoutSeconds = 0;

    /**
     * lock的过期时间，单位为秒
     * 大于0 表示设置秒数
     * 设置-1表示无限时间
     */
    @Setter
    @Getter
    protected int lockTimeoutSeconds = 0;


    /**
     * 如果返回值是null且cacheNullAble=true时
     * 会设置此值到缓存中，返回时也自动转换成null
     */
    private static final String CACHE_NULL = "_CACHE_NULL";

    /**
     * 设置锁的值
     */
    private static final int LOCK = 1;

    /**
     * cache 默认timeout时间5分钟
     */
    private static final long CACHE_DEFAULT_TIME_OUT = 5 * 60L;

    /**
     * lock 默认timeout时间10秒
     */
    private static final long LOCK_DEFAULT_TIME_OUT = 10L;

    /**
     * 提供给子类设置的 redisConnectionFactory
     *
     * @param redisConnectionFactory
     */
    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
        initDefaultRedisTemplate(redisConnectionFactory);
    }

    @Around("@annotation(me.ele.demo.aop.annotition.CacheAuto)")
    public Object cacheAuto(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取真正的实现类的方法
        Method implMethod = AspectSupportUtils.getTargetCalssMethod(joinPoint);
        //获取方法上的注解
        CacheAuto cacheAuto = implMethod.getDeclaredAnnotation(CacheAuto.class);
        boolean isReturnVoid = Void.TYPE == implMethod.getReturnType();
        //void类型时 不能使用 AUTO，DELETE_REPLACE，REPLACE
        if (isReturnVoid && (cacheAuto.type() == CacheType.AUTO || cacheAuto.type() == CacheType.REPLACE ||
                cacheAuto.type() == CacheType.DELETE_REPLACE)) {
            log.warn("CacheType.AUTO,REPLACE,DELETE_REPLACE not support return type [void]");
            return joinPoint.proceed();
        }
        log.debug("cacheAuto:{}", cacheAuto);
        //自动解析el表达式的key
        String finalKey = autoParseElKey(cacheAuto.key(), cacheAuto.keyPrefix(), implMethod, joinPoint);
        //AUTO，如果取得缓存直接返回，不执行实际方法，
        //DELETE,DELETE_REPLACE：将先删除缓存
        if (cacheAuto.type() == CacheType.AUTO) {
            Object obj = redisTemplateCache.opsForValue().get(finalKey);
            if (obj != null) {
                if (isCacheNullAble() && CACHE_NULL.equals(obj)) {
                    log.debug("loaded from cached,{}=null", finalKey);
                    return null;
                }
                log.debug("loaded from cache,{}={}", finalKey, obj);
                return obj;
            }
        } else if (cacheAuto.type() == CacheType.DELETE || cacheAuto.type() == CacheType.DELETE_REPLACE) {
            log.debug("delete:{}", finalKey);
            redisTemplateCache.delete(finalKey);
        }
        //执行方法
        Object returnObj = joinPoint.proceed();
        //如果是void方法，则直接返回
        if (isReturnVoid) {
            return returnObj;
        }
        afterMethodSetCache(cacheAuto, finalKey, returnObj);
        return returnObj;
    }


    /**
     * 分布式锁aop实现方法
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(me.ele.demo.aop.annotition.LockAuto)")
    public Object lockAuto(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取真正的实现类的方法
        Method implMethod = AspectSupportUtils.getTargetCalssMethod(joinPoint);
        //获取方法上的注解
        LockAuto lockAuto = implMethod.getDeclaredAnnotation(LockAuto.class);
        //自动解析el表达式的key
        String finalKey = autoParseElKey(lockAuto.key(), lockAuto.keyPrefix(), implMethod, joinPoint);
        boolean setLockResult = redisTemplateLock.opsForValue().setIfAbsent(finalKey, LOCK);
        if (!setLockResult) {
            log.warn("set lock failed ,lock key:{}", finalKey);
            //实际场景中最好使用公司的框架定义的异常
            //throw new ServiceException(CommonExceptionEnum.E010011.name(), CommonExceptionEnum.E010011.getMessage());
            throw new Exception("锁已被占用，稍候再试");
        }
        try {
            //设置超时时间
            setLockExpire(finalKey, lockAuto);
            return joinPoint.proceed();
        } finally {
            log.info("delete lock :{}", finalKey);
            redisTemplateCache.delete(finalKey);
        }
    }

    /**
     * 自动解析key
     * 如果不包含#号直接返回，
     * 否则解析el表达式得到最终的key
     *
     * @param key        原始未解析的key
     * @param prefix     前缀
     * @param implMethod 代理的方法
     * @param joinPoint
     * @return
     */
    private String autoParseElKey(String key, String prefix, Method implMethod, ProceedingJoinPoint joinPoint) {
        if (!key.contains("#")) {
            //如果#号就去解析了，直接返回前缀+key
            return prefix.concat(key);
        }
        //有#号需要解析el表达式,解析el表达式,将#id等替换为参数值,解析el表达式
        String parseElKey = AspectSupportUtils.parseElKey(key, implMethod, joinPoint.getArgs());
        log.debug("parse el key:{}->{}", key, parseElKey);
        return prefix.concat(parseElKey);
    }

    /**
     * 方法执行之后执行缓存设置
     * 只会设置以下类型
     * case AUTO:
     * case REPLACE:
     * case DELETE_REPLACE:
     *
     * @param cacheAuto
     * @param finalKey
     * @param returnObj
     */
    private void afterMethodSetCache(CacheAuto cacheAuto, String finalKey, Object returnObj) {
        //非void类型方法执行完成后，将设置
        switch (cacheAuto.type()) {
            //AUTO，REPLACE，DELETE_REPLACE，设置缓存
            case AUTO:
            case REPLACE:
            case DELETE_REPLACE:
                if (returnObj == null && isCacheNullAble()) {
                    setCache(finalKey, cacheAuto, CACHE_NULL);
                } else {
                    setCache(finalKey, cacheAuto, returnObj);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 设置缓存
     * 优先使用cacheAuto里的时间，其次使用#getTimeoutSeconds()
     * 如果都没有 取 DEFAULT_TIME_OUT
     * -1表示永不过期
     *
     * @param finalKey
     * @param cacheAuto
     * @param obj
     */
    private void setCache(String finalKey, CacheAuto cacheAuto, Object obj) {
        long timeout = CACHE_DEFAULT_TIME_OUT;
        if (cacheAuto.timeOutSeconds() != 0) {
            timeout = cacheAuto.timeOutSeconds();
        } else if (getCacheTimeoutSeconds() != 0) {
            timeout = getCacheTimeoutSeconds();
        }
        if (timeout > 0) {
            log.debug("key:{},timeout:{} secs,value:{}", finalKey, timeout, obj);
            redisTemplateCache.opsForValue().set(finalKey, obj, timeout, TimeUnit.SECONDS);
            return;
        }
        //如果为负数的情况，表示无限时间，统一设置成-1
        log.debug("key:{},timeout:{} [no timeout] ,value:{}", finalKey, timeout, obj);
        redisTemplateCache.opsForValue().set(finalKey, obj);

    }


    /**
     * 设置lock过期时间
     * 优先使用lockAuto里的时间，其次使用#getLockTimeoutSeconds()
     * 如果都没有 取 DEFAULT_TIME_OUT
     * -1表示永不过期
     *
     * @param finalKey
     * @param lockAuto
     */
    private void setLockExpire(String finalKey, LockAuto lockAuto) {
        long timeout = LOCK_DEFAULT_TIME_OUT;
        if (lockAuto.timeOutSeconds() > 0) {
            timeout = lockAuto.timeOutSeconds();
        } else if (getLockTimeoutSeconds() > 0) {
            timeout = getLockTimeoutSeconds();
        }
        log.debug("lock:{},timeout:{} secs", finalKey, timeout);
        redisTemplateLock.expire(finalKey, timeout, TimeUnit.SECONDS);
        //如果<0不设置超时，非常危险
        log.warn("lock key:{} not set timeout, not recommended",
                finalKey);
    }

    /**
     * 生成一个redisTemplateCache，redisTemplateLock
     * 在设置完RedisConnectionFactory的时候自动调用
     *
     * @param redisConnectionFactory
     */
    private void initDefaultRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //生成默认的redisTemplate用于cache，一般也不建议用户设置redisTemplate
        redisTemplateCache = new RedisTemplate<String, String>();
        redisTemplateCache.setConnectionFactory(redisConnectionFactory);
        redisTemplateCache.setKeySerializer(new GenericToStringSerializer(Object.class));
        redisTemplateCache.setValueSerializer(new GenericFastJsonRedisSerializer());
        redisTemplateCache.setDefaultSerializer(new GenericFastJsonRedisSerializer());
        redisTemplateCache.afterPropertiesSet();
        log.debug("default redisTemplateCache inited:{}", redisTemplateCache);

        //生成默认的redisTemplate用于lock，一般也不建议用户设置redisTemplate
        redisTemplateLock = new RedisTemplate<String, String>();
        redisTemplateLock.setConnectionFactory(redisConnectionFactory);
        redisTemplateLock.setKeySerializer(new GenericToStringSerializer(Object.class));
        redisTemplateLock.setValueSerializer(new GenericToStringSerializer(Integer.class));
        redisTemplateLock.afterPropertiesSet();
        log.debug("default redisTemplateLock inited:{}", redisTemplateLock);

    }
}