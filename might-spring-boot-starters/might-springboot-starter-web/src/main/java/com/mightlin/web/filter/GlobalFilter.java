package com.mightlin.web.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mightlin.common.constant.LogConstant;
import com.mightlin.common.log.AccessLogHandler;
import com.mightlin.web.http.HttpRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 全局过滤器
 */
@Slf4j
@Component
public class GlobalFilter extends OncePerRequestFilter {
    private final AccessLogHandler accessLogHandler = SpringUtil.getBean(AccessLogHandler.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        MDC.put(LogConstant.TRACE_ID, IdUtil.objectId());
        try {
            // 执行过滤链
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Throwable throwable) {
            log.error("请求发生异常:", throwable);
            throw throwable;
        } finally {
            // 记录访问日志
            accessLogHandler.handlerAccessLog(requestWrapper, responseWrapper, 100L);
            // 重新写入数据到响应信息中
            responseWrapper.copyBodyToResponse();
            // 删除日志tranceId
            MDC.remove(LogConstant.TRACE_ID);
        }
    }

}
