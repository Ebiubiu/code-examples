package me.ele.demo.business.aop;

import me.ele.demo.aop.aspject.RedisCacheLockAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;


/**
 * 缓存AOP
 * 使用@CacheAuto注解
 * 使用@LockAuto注解
 * @author daiderong
 * @see me.ele.demo.aop.annotition.CacheAuto
 * @see me.ele.demo.aop.annotition.LockAuto
 * @see RedisCacheLockAspect
 */
@Component
public class MyRedisCacheAspect extends RedisCacheLockAspect {

    @Autowired
    @Override
    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        super.setRedisConnectionFactory(redisConnectionFactory);
    }

    @Value("true")
    @Override
    public void setCacheNullAble(boolean cacheNullAble) {
        super.setCacheNullAble(cacheNullAble);
    }

    @Value("180")
    @Override
    public void setCacheTimeoutSeconds(int expireSeconds) {
        super.setCacheTimeoutSeconds(expireSeconds);
    }

    @Value("10")
    @Override
    public void setLockTimeoutSeconds(int expireSeconds) {
        super.setCacheTimeoutSeconds(expireSeconds);
    }


}