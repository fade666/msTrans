package demo.mawei.target;

import java.lang.annotation.*;

/**
 * @Author Maw
 * @Date 2021/7/18 22:25
 * @Version 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedObject {
    //不需要参数
}
