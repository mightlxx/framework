package com.mightlin.common.mybatis;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface RoutingWith {

    DynamicDataSourceEnum value() default DynamicDataSourceEnum.MASTER;

}
