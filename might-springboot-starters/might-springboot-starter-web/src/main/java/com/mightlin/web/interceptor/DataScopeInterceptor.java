package com.mightlin.web.interceptor;

import com.mightlin.common.datascope.DataScope;
import com.mightlin.common.datascope.DataScopeUtil;
import com.mightlin.common.model.CommonConstant;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataScopeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            DataScope dataScope = ((HandlerMethod) handler).getMethod().getAnnotation(DataScope.class);
            if (dataScope != null) {
                request.setAttribute(CommonConstant.DATA_SCOPE_SQL, DataScopeUtil.getFilterSql(dataScope));
            }
        }
        return true;
    }
}
