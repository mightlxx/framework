package com.mightlin.common.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

@Data
public class PageParam {

    /**
     * 当前页码  从 1 开始
     */
    @Min(value = 1, message = "当前页不能小于 1")
    private long current = 1;

    /**
     * 每页显示条数 最大值为 100
     */
    @Range(min = 1, max = 100, message = "条数范围为 [1, 100]")
    private long size = 10;
}
