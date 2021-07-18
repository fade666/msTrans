package demo.mawei.target;

import java.lang.annotation.*;

/**
 * @Author Maw
 * @Date 2021/7/18 22:24
 * @Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheLock {
    String lockedPrefix() default "";//redis 锁key的前缀
    long timeOut() default 2000;//轮询锁的时间
    int expireTime() default 1000;//key在redis里存在的时间，1000S
}
