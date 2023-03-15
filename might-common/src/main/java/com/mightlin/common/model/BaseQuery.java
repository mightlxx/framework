package com.mightlin.common.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseQuery<Q> {

    /**
     * 查询参数   范围查询[d,2]  模糊查询like_xxx
     */
    private Q searchParam;

    /**
     * 分页参数
     */
    private PageParam pageParam;

    /**
     * 查询字段
     */
    private List<String> selectFieldList;

    /**
     * 排序字段--格式字段名称加_asc或者_desc例：createTime_asc,createTime_desc
     */
    private List<String> sortFieldList = new ArrayList<>();

    /**
     * 查询器
     */
    private transient QueryWrapper queryWrapper;

}

