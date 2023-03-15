package com.mightlin.web.log;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.mightlin.common.constant.LogConstant;
import com.mightlin.common.log.AccessLogHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DefaultAccessLogHandler implements AccessLogHandler<AccessLog> {

    private final AntPathMatcher antMatcher = new AntPathMatcher();
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Value("#{'${web.ignore-access-log-paths:}'.split(',')}")
    private List<String> ignoreUrls = ListUtil.toList();

    @Override
    public boolean skipUrl(HttpServletRequest request) {
        return ignoreUrls.stream().anyMatch(url -> antMatcher.match(url, urlPathHelper.getLookupPathForRequest(request)));
    }

    @Override
    public AccessLog buildAccessLog(HttpServletRequest request, HttpServletResponse response, Long executionTime) {
        AccessLog log = AccessLog.builder()
                .traceId(MDC.get(LogConstant.TRACE_ID))
                .uri(request.getRequestURI())
                .method(request.getMethod())
                .ip(ServletUtil.getClientIP(request))
                .requestParams(this.getRequestParams(request))
                .requestBody(this.getRequestBody(request))
                .result(this.getResponseBody(response))
                .executionTime(executionTime)
                .build();
        return log;
    }

    @Override
    public void saveLog(AccessLog accessLog) {
        log.info(accessLog.toString());
    }

    private String getResponseBody(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            // 获取响应体
            byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
            return new String(contentAsByteArray, StandardCharsets.UTF_8);
        } else {
            log.warn("未包装响应体，请检查！");
        }
        return "";
    }

    private String getRequestParams(HttpServletRequest httpRequest) {
        Map<String, String> paramMap = ServletUtil.getParamMap(httpRequest);
        return paramMap.toString();
    }

    private String getRequestBody(HttpServletRequest httpRequest) {
        if (httpRequest instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper responseWrapper = (ContentCachingRequestWrapper) httpRequest;
            // 获取响应体
            byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
            return new String(contentAsByteArray, StandardCharsets.UTF_8);
        } else {
            log.warn("未包装请求体，请检查！");
        }
        return "";
    }
}
