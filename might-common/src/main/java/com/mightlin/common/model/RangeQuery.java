package com.mightlin.common.model;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RangeQuery {
    /**
     * 范围查询的字段
     */
    @Pattern(regexp = "[A-Za-z0-9_]{1,64}", message = "范围查询的字段格式非法")
    private String field;
    /**
     * 起始数值
     */
    private Long start = 0L;

    /**
     * 起始字段关系符 le lt  ge gt
     */
    private String startSymbol;
    /**
     * 结束数值
     */
    private Long end = 0L;

    /**
     * 结束字段关系符
     */
    private String endSymbol;
}
