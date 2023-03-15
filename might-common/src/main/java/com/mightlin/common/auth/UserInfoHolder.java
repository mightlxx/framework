package com.mightlin.common.auth;

public class UserInfoHolder {

    /**
     * 保存用户对象的ThreadLocal
     */
    private static final ThreadLocal<BaseUser> userThreadLocal = new ThreadLocal<>();

    /**
     * 添加当前登录用户方法
     */
    public static void addCurrentUser(BaseUser user) {
        userThreadLocal.set(user);
    }

    public static BaseUser getCurrentUser() {
        return userThreadLocal.get();
    }

    /**
     * 防止内存泄漏
     */
    public static void remove() {
        userThreadLocal.remove();
    }
}
