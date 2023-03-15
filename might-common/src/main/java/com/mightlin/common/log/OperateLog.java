package com.mightlin.common.log;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface OperateLog {

    // 操作内容
    String value() default "";

    /**
     * 标题 优先于  {@link OperateLogTitle }
     */
    String title() default "";
}
