package com.mightlin.mybatis.datasource;

import com.mightlin.common.mybatis.DynamicDataSourceEnum;
import com.mightlin.common.mybatis.RoutingWith;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Aspect
@Component
public class RoutingAspect {

    @Pointcut("@annotation(com.mightlin.common.mybatis.RoutingWith)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object routingWithDataSource(ProceedingJoinPoint joinPoint) throws Throwable {
        String datasourceName = getDatasourceName(joinPoint);
        DynamicDataSource.setDataSource(datasourceName);
        log.debug("set datasource is " + datasourceName);
        try {
            return joinPoint.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            log.debug("clean datasource");
        }
    }

    private String getDatasourceName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 有事务注解使用主库
        Transactional transactionalAnnotation = AnnotationUtils.findAnnotation(signature.getMethod(), Transactional.class);
        if (transactionalAnnotation != null){
            return DynamicDataSourceEnum.MASTER.getDataSourceName();
        }
        RoutingWith annotationInMethod = AnnotationUtils.findAnnotation(signature.getMethod(), RoutingWith.class);
        if (annotationInMethod != null) {
            return annotationInMethod.value().getDataSourceName();
        }
        RoutingWith annotationInClass = AnnotationUtils.findAnnotation(signature.getClass(), RoutingWith.class);
        if (annotationInClass != null) {
            return annotationInMethod.value().getDataSourceName();
        }
        return DynamicDataSourceEnum.MASTER.getDataSourceName();
    }
}
