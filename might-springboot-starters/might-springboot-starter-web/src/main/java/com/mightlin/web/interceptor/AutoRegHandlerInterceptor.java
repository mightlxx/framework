package com.mightlin.web.interceptor;

import cn.hutool.core.collection.ListUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * 自动注册的拦截器
 */
public interface AutoRegHandlerInterceptor extends HandlerInterceptor {

    List<String> getPathPatterns();

    default List<String> getExcludePathPatterns() {
        return ListUtil.empty();
    }
}
