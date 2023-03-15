package com.mightlin.web.api;

import com.mightlin.common.model.BaseQuery;
import com.mightlin.common.model.IdListModel;
import com.mightlin.common.model.IdModel;
import com.mightlin.common.model.QueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface BaseApi<C, Q, V> {

    /**
     * 新增
     *
     * @param cmd
     * @return
     */
    @PostMapping("/create")
    void create(@Valid @RequestBody C cmd);

    /**
     * 修改
     *
     * @param cmd
     * @return
     */
    @PostMapping("/update")
    void update(@RequestBody @Valid C cmd);


    /**
     * 删除
     *
     * @param idModel
     */
    @PostMapping("/remove")
    void removeById(@Valid @RequestBody IdModel<Long> idModel);

    /**
     * 删除
     *
     * @param idListModel
     * @return
     */
    @PostMapping("/removeBatchByIdList")
    void removeBatchByIdList(@Valid @RequestBody IdListModel<Long> idListModel);

    /**
     * 基础查询
     *
     * @param query
     * @return
     */
    @PostMapping("/query")
    QueryResult<V> query(@Valid @RequestBody BaseQuery<Q> query);

}
