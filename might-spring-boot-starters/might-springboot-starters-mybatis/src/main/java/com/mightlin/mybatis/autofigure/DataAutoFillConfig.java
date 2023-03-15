package com.mightlin.mybatis.autofigure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.mightlin.common.auth.BaseUser;
import com.mightlin.common.auth.UserInfoHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataAutoFillConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        BaseUser currentUser = UserInfoHolder.getCurrentUser();
        this.strictInsertFill(metaObject, "createBy", Long.class, currentUser != null ? currentUser.getId() : null);
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        BaseUser currentUser = UserInfoHolder.getCurrentUser();
        this.strictUpdateFill(metaObject, "updateBy", Long.class, currentUser != null ? currentUser.getId() : null);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
