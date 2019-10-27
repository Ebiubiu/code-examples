package me.ele.demo.aop.annotition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解使用
 *
 * @author daiderong
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheAuto {


    /**
     * 缓存操作类型 -- 必须，不设置默认 auto
     * 有缓存时读缓存，无缓存时执行完方法自动设置缓存
     *
     * @return
     */
    CacheType type() default CacheType.AUTO;

    /**
     * 设置key，支持el表达式
     * 必须设置
     *
     * @return
     */
    String key() default "";

    /**
     * 失效秒数 -- 可选参数，不设置时会取切面的默认时间
     * -1表示永久
     *
     * @return
     */
    int timeOutSeconds() default 0;


    /**
     * key的前缀，不支持el表达式，是常量
     * 如 "USER_NAME_"
     *
     * @return
     */
    String keyPrefix() default "";


}