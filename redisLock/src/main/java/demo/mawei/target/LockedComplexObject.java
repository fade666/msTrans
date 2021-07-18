package demo.mawei.target;

import java.lang.annotation.*;

/**
 * @Author Maw
 * @Date 2021/7/18 22:26
 * @Version 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedComplexObject {
    String field() default "";//含有成员变量的复杂对象中需要加锁的成员变量，如一个商品对象的商品ID
}
