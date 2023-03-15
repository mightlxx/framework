package com.mightlin.common.log;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface OperateLogField {

    String name() default "";

    String dictCode() default "";

    /**
     * 字段内容转换器
     *
     * @return
     */
    Class<? extends FieldLogConverter> converter() default FieldLogConverter.class;


}
