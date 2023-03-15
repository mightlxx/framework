package com.mightlin.common.auth;

import java.util.List;
import java.util.Set;

public interface BaseUser {
    /**
     * 获取id
     * @return
     */
    Long getId();
    /**
     * 用户名
     *
     * @return
     */
    String getUsername();

    /**
     * 是否超级管理员
     *
     * @return
     */
    Boolean getSuperAdminFlag();

    /**
     * 拥有权限集合
     */
    Set<String> getPermPathSet();

    /**
     * 数据权限范围
     * <p>
     * null：表示全部数据权限
     */
    List<Long> getDataScopeList();
}
