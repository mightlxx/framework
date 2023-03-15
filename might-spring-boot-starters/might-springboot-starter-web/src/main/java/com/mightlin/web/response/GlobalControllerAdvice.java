package com.mightlin.web.response;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mightlin.common.model.BaseResponse;
import com.mightlin.common.response.BusinessException;
import com.mightlin.common.response.SystemResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一响应结果
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice implements ResponseBodyAdvice<Object> {

    @Value("#{'${web.ignore-body-paths:}'.split(',')}")
    private List<String> ignoreBodyPaths = ListUtil.toList();

    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = methodParameter.getMethod();
        Class<?> clz = methodParameter.getDeclaringClass();
        if (method == null) {
            return false;
        }
        // 检查注解是否存在
        return !(clz.isAnnotationPresent(IgnoreBodyAdvice.class) || method.isAnnotationPresent(IgnoreBodyAdvice.class));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (ignoreBodyPaths.contains(request.getURI().getPath())) {
            return body;
        }
        if (body instanceof BaseResponse) {
            return body;
        }
        // String类型不能直接包装
        if (returnType.getGenericParameterType().equals(String.class)) {
            // 将数据包装在ResultVo里后转换为json串进行返回
            return JSONUtil.toJsonStr(BaseResponse.ok(body));
        }
        // 否则直接包装成BaseResponse返回
        return BaseResponse.ok(body);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
        // 处理日期字段解析绑定
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(DateUtil.parse(text));
            }
        });
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        });
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        });
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public BaseResponse handleBusinessException(BusinessException e) {
        log.debug("业务处理异常，{}", e.getMessage(), e);
        return BaseResponse.failed(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验(Valid)异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public BaseResponse handleValidException(MethodArgumentNotValidException e) {
        log.debug("数据校验异常，{}，异常类型：{}", e.getMessage(), e.getClass());
        List<String> errList = e.getFieldErrors().stream().map(error -> error.getField() + ":" + error.getDefaultMessage()).collect(Collectors.toList());
        return BaseResponse.failed(SystemResultCode.BAD_REQUEST, StrUtil.join(",", errList));
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(value = {BindException.class})
    public BaseResponse handleValidException(BindException e) {
        log.debug("数据校验异常，{}，异常类型：{}", e.getMessage(), e.getClass());
        List<String> errList = e.getFieldErrors().stream().map(error -> error.getField() + ":" + error.getDefaultMessage()).collect(Collectors.toList());
        return BaseResponse.failed(SystemResultCode.BAD_REQUEST, StrUtil.join(",", errList));
    }

    /**
     * 约束校验异常
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public BaseResponse handleValidException(ConstraintViolationException e) {
        log.debug("数据校验异常，{}，异常类型：{}", e.getMessage(), e.getClass());
        List<String> violations = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        String error = violations.get(0);
        return BaseResponse.failed(SystemResultCode.PARAM_ERROR, error);
    }

    /**
     * 未知异常
     */
    @ExceptionHandler(value = Throwable.class)
    public BaseResponse handleException(Throwable t) {
        log.error("系统异常:", t);
        return BaseResponse.failed();
    }
}
