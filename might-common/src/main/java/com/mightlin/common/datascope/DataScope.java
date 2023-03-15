package com.mightlin.common.datascope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface DataScope {
    String tableAlias() default "";
    String orgIdAlias() default "org_id";
    String creatorAlias() default "creator";
}
