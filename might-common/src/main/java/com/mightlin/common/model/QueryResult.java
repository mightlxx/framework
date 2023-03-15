package com.mightlin.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果
 *
 * @param <V>
 */
@Data
public class QueryResult<V> implements Serializable {

    private static final long serialVersionUID = 9056411043515781783L;

    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    /**
     * 查询数据列表
     */
    private List<V> records;

    public QueryResult(List<V> records) {
        this.records = records;
    }

    public QueryResult(List<V> records, PageInfo pageInfo) {
        this.records = records;
        this.pageInfo = pageInfo;
    }

    public static <V> QueryResult<V> of(IPage<V> page) {
        return new QueryResult(page.getRecords(), new PageInfo(page.getCurrent(), page.getSize(), page.getTotal(), page.getPages()));
    }

    public static <V> QueryResult<V> of(List<V> records) {
        return new QueryResult(records);
    }

}

