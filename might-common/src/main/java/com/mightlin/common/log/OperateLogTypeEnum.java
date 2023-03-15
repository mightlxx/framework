package com.mightlin.common.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperateLogTypeEnum {
    CREATE("创建"),
    UPDATE("更新"),
    DELETE("删除"),
    NULL("NULL");
    private final String desc;
}
