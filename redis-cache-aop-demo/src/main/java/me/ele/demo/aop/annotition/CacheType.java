package me.ele.demo.aop.annotition;

/**
 * 缓存注解使用
 *
 * @author daiderong
 */
public enum CacheType {

    /**
     * 有缓存时直接使用缓存，无缓存时，执行完方法设置缓存
     */
    AUTO,

    /**
     * 执行方法完成后替替换
     */
    REPLACE,

    /**
     * 执行方法前先清除，方法完成后替换
     */
    DELETE_REPLACE,


    /**
     * 直接清除缓存
     */
    DELETE


}