package com.mightlin.web.interceptor;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mightlin.common.auth.BaseUser;
import com.mightlin.common.auth.UserInfoHolder;
import com.mightlin.common.response.BusinessException;
import com.mightlin.common.response.SystemResultCode;
import com.mightlin.web.auth.AuthHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 权限校验
 */
@Component
public class AuthInterceptor implements AutoRegHandlerInterceptor {

    private final AuthHandler authHandler = SpringUtil.getBean(AuthHandler.class);

    @Override
    public List<String> getPathPatterns() {
        return ListUtil.toList("/**");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean skip = authHandler.skipLoginAuth(request.getRequestURI());
        if (skip) {
            return true;
        }
        BaseUser loginUser = authHandler.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(SystemResultCode.UNAUTHORIZED);
        }
        // 用户信息加入线程上下文
        UserInfoHolder.addCurrentUser(loginUser);
        // 接口权限校验
        if (handler instanceof HandlerMethod) {
            authHandler.permAuth(request, (HandlerMethod) handler, loginUser);
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 防止内存溢出
        UserInfoHolder.remove();
    }
}
