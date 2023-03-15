package com.mightlin.common.log;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface OperateLogTitle {

    // 操作标题
    String value() default "";

}
