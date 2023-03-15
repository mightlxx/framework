package com.mightlin.web.auth;

import cn.hutool.core.collection.CollectionUtil;
import com.mightlin.common.auth.BaseUser;
import com.mightlin.common.response.BusinessException;
import com.mightlin.common.response.SystemResultCode;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AuthHandler {

    AntPathMatcher antMatcher = new AntPathMatcher();
    UrlPathHelper urlPathHelper = new UrlPathHelper();

    /**
     * 获取登录用户
     */
    BaseUser getLoginUser(HttpServletRequest request);

    /**
     * 跳过登录检验
     *
     * @param path
     * @return
     */
    default boolean skipLoginAuth(String path) {
        if (CollectionUtil.isEmpty(getSkipLoginAuthPaths())) {
            return true;
        }
        return getSkipLoginAuthPaths().contains(path);
    }

    /**
     * 跳过登录校验集合
     *
     * @return
     */
    List<String> getSkipLoginAuthPaths();

    /**
     * 对 Controller 做权限校验
     *
     * @param request
     * @param handlerMethod
     */
    default void permAuth(HttpServletRequest request, HandlerMethod handlerMethod, BaseUser loginUser) {
        boolean hasPerm = loginUser.getSuperAdminFlag() ||
                loginUser.getPermPathSet().stream().anyMatch(urlPattern -> antMatcher.match(urlPattern, urlPathHelper.getLookupPathForRequest(request)));
        RequirePerm methodAnnotation = handlerMethod.getMethodAnnotation(RequirePerm.class);
        if (methodAnnotation != null && !hasPerm) {
            throw new BusinessException(SystemResultCode.FORBIDDEN);
        }
        RequirePerm clazzAnnotation = handlerMethod.getBeanType().getAnnotation(RequirePerm.class);
        if (clazzAnnotation != null && !hasPerm) {
            throw new BusinessException(SystemResultCode.FORBIDDEN);
        }
    }

}
