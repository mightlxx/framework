package com.mightlin.common.datascope;

import cn.hutool.core.util.StrUtil;
import com.mightlin.common.auth.BaseUser;
import com.mightlin.common.auth.UserInfoHolder;
import com.mightlin.common.response.BusinessException;
import com.mightlin.common.response.SystemResultCode;

import java.util.List;

public class DataScopeUtil {

    public static String getFilterSql(DataScope dataScope) {
        if (dataScope == null) {
            return null;
        }
        String tableAlias = dataScope.tableAlias();
        String orgIdAlias = dataScope.orgIdAlias();
        BaseUser user = UserInfoHolder.getCurrentUser();
        if (user == null){
            throw  new BusinessException(SystemResultCode.UNAUTHORIZED);
        }
        // 如果是超级管理员，则不进行数据过滤
        if (user.getSuperAdminFlag()) {
            return null;
        }

        // 如果为null，则设置成空字符串
        if (tableAlias == null) {
            tableAlias = "";
        }

        // 获取表的别名
        if (StrUtil.isNotBlank(tableAlias)) {
            tableAlias += ".";
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        // 数据权限范围
        List<Long> dataScopeList = user.getDataScopeList();
        // 全部数据权限
        if (dataScopeList == null) {
            return null;
        }
        // 数据过滤
        if (dataScopeList.size() > 0) {
            if (StrUtil.isBlank(orgIdAlias)) {
                orgIdAlias = "org_id";
            }
            sqlFilter.append(tableAlias).append(orgIdAlias);

            sqlFilter.append(" in(").append(StrUtil.join(",", dataScopeList)).append(")");

            sqlFilter.append(" or ");
        }

        // 查询本人数据
        sqlFilter.append(tableAlias).append("creator").append("=").append(user.getId());

        sqlFilter.append(")");

        return sqlFilter.toString();
    }

}
