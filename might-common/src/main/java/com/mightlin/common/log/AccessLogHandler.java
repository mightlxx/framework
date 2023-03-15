package com.mightlin.common.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

public interface AccessLogHandler<T> {

    boolean skipUrl(HttpServletRequest request);

    default void handlerAccessLog(HttpServletRequest request, HttpServletResponse response, Long executionTime) {
        // 跳过忽略访问日志url
        if (this.skipUrl(request)) {
            return;
        }
        T accessLog = buildAccessLog(request, response, executionTime);
        saveLog(accessLog);
    }

    /**
     * 构建访问日志
     *
     * @param request
     * @param response
     * @param executionTime
     * @return
     */
    T buildAccessLog(HttpServletRequest request, HttpServletResponse response, Long executionTime);

    /**
     * 保存
     *
     * @param accessLog
     */
    void saveLog(T accessLog);

}
