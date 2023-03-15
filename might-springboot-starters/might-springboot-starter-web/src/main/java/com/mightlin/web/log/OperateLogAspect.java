package com.mightlin.web.log;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mightlin.common.log.OperateLog;
import com.mightlin.common.log.OperateLogHandler;
import com.mightlin.common.log.OperateLogTitle;
import com.mightlin.common.log.OptLogFieldContentThreadLocal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperateLogAspect<T> {

    private final OperateLogHandler<T> operateLogHandler = SpringUtil.getBean(OperateLogHandler.class);

    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)" +
            "|| within(com.mightlin.web.appservice.BaseAppService+) || @annotation(com.mightlin.common.log.OperateLog)")
    public void operateLogPointcut() {
    }

    // 后置通知
    @AfterReturning("operateLogPointcut()")
    public void doOperateLog(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 操作日志
        OperateLog methodAnnotation = AnnotationUtil.getAnnotation(method, OperateLog.class);
        if (methodAnnotation == null) {
            return;
        }
        String title;
        if (StrUtil.isNotBlank(methodAnnotation.title())) {
            title = methodAnnotation.title();
        } else {
            OperateLogTitle titleAnnotation = AnnotationUtil.getAnnotation(joinPoint.getTarget().getClass(), OperateLogTitle.class);
            title = titleAnnotation != null ? titleAnnotation.value() : "";
        }
        operateLogHandler.handlerLogOptLog(title, methodAnnotation.value());
        // 移除操作日志字段内容
        OptLogFieldContentThreadLocal.remove();
    }
}
