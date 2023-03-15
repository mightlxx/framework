package com.mightlin.web.appservice;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mightlin.common.domain.converter.ModelConverter;
import com.mightlin.common.domain.event.DomainEventBus;
import com.mightlin.common.domain.model.DomainObj;
import com.mightlin.common.log.OperateLog;
import com.mightlin.common.model.*;
import com.mightlin.common.util.GenericUtil;
import com.mightlin.common.util.ReflectUtil;
import com.mightlin.web.api.BaseApi;
import com.mightlin.web.auth.RequirePerm;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BaseAppService<D, C, Q, V, CV extends ModelConverter<D, C, Q, V>,
        S extends IService> implements BaseApi<C, Q, V> {

    // 消息总线
    protected DomainEventBus getEventBus() {
        return SpringUtil.getBean(DomainEventBus.class);
    }

    private final Class<S> serviceClass = GenericUtil.getGenericClass(this.getClass(), "S");
    private final Class<CV> modelConverterClass = GenericUtil.getGenericClass(this.getClass(), "CV");

    private final List<String> selectFields = ReflectUtil.getFieldNames(GenericUtil.getGenericClass(this.getClass(), "V"));
    private final Map<String, String> entityColumnMap;

    protected S getService() {
        return SpringUtil.getBean(serviceClass);
    }

    protected CV getConverter() {
        return SpringUtil.getBean(modelConverterClass);
    }


    public BaseAppService() {
        TableInfo info = TableInfoHelper.getTableInfo(this.getService().getEntityClass());
        List<TableFieldInfo> fieldList = info.getFieldList();
        entityColumnMap = fieldList.stream().collect(Collectors.toMap(TableFieldInfo::getProperty, TableFieldInfo::getColumn));
    }

    /**
     * 新增
     *
     * @param cmd
     */
    @RequirePerm
    @OperateLog(value = "新增")
    public void create(@Valid @RequestBody C cmd) {
        D d = this.getConverter().cmdToEntity(cmd);
        this.getService().save(d);
    }

    /**
     * 修改
     *
     * @param cmd
     */
    @RequirePerm
    @OperateLog("修改")
    public void update(@RequestBody @Valid C cmd) {
        D d = this.getConverter().cmdToEntity(cmd);
        this.getService().updateById(d);
    }

    /**
     * 删除
     *
     * @param idModel
     */
    @RequirePerm
    @Transactional(rollbackFor = Exception.class)
    public void removeById(@Valid @RequestBody IdModel<Long> idModel) {
        this.getService().removeById(idModel.getId());
    }

    /**
     * 批量删除
     *
     * @param idListModel
     */
    @RequirePerm
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchByIdList(@Valid @RequestBody IdListModel<Long> idListModel) {
        this.getService().removeBatchByIds(idListModel.getIdList());
    }

    /**
     * 查询
     *
     * @param query
     * @return
     */
    public QueryResult<V> query(@Valid @RequestBody BaseQuery<Q> query) {
        PageParam pageParam = query.getPageParam();
        // 分页查询
        if (ObjectUtil.isNotNull(pageParam)) {
            Page<D> page = Page.of(pageParam.getCurrent(), pageParam.getSize());
            this.getService().page(page, buildQueryWrapper(query));
            return QueryResult.of(page.convert(p -> this.getConverter().entityToVo(p)));
        }
        // 非分页查询
        List<D> voList = this.getService().list(buildQueryWrapper(query));
        return QueryResult.of(voList.stream().map(d -> this.getConverter().entityToVo(d)).collect(Collectors.toList()));
    }

    private QueryWrapper buildQueryWrapper(BaseQuery<Q> query) {
        QueryWrapper<D> queryWrapper;
        if (query.getQueryWrapper() != null) {
            queryWrapper = query.getQueryWrapper();
        } else {
            // 等值查询
            queryWrapper = buildParamWrapper(query.getSearchParam());
        }
        // 查询字段处理
        this.selectFiledFilter(queryWrapper, query.getSelectFieldList());
        // 排序处理
        this.handleSortQuery(queryWrapper, query.getSortFieldList());
        return queryWrapper;
    }

    private void handleSortQuery(QueryWrapper queryWrapper, List<String> sortList) {
        if (CollUtil.isEmpty(sortList)) {
            return;
        }
        sortList.stream().filter(StrUtil::isNotEmpty).forEach(sortFiled -> {
            List<String> filedStrArr = StrUtil.split(sortFiled, "_");
            String column = entityColumnMap.get(filedStrArr.get(0));
            // 正排序字段
            if (filedStrArr.size() > 1 && filedStrArr.get(1).equals("asc")) {
                queryWrapper.orderByAsc(StrUtil.isNotEmpty(column), column);
            }
            // 倒排序字段
            if (filedStrArr.size() > 1 && filedStrArr.get(1).equals("desc")) {
                queryWrapper.orderByDesc(StrUtil.isNotEmpty(column), column);
            }
        });
    }

    /**
     * 构建等值查询
     *
     * @param qo
     * @return
     */
    protected QueryWrapper<D> buildParamWrapper(Q qo) {
        if (qo == null) {
            return Wrappers.query();
        }
        // 等值查询 过滤掉实体中没有的字段
        Map<String, Object> paramMap = BeanUtil.beanToMap(qo);
        QueryWrapper queryWrapper = Wrappers.query();
        paramMap.forEach((k, v) -> {
            String[] fieldStrArr = k.split("_");
            String filed = fieldStrArr[0];
            String symbol = fieldStrArr.length > 1 ? fieldStrArr[1] : "";
            String column = entityColumnMap.get(filed);
            // entity里的字段才加入查询条件
            if (StrUtil.isNotBlank(column)) {
                // 字段名后缀做不同条件查询
                queryWrapper.gt("gt".equals(symbol) && ObjectUtil.isNotEmpty(v), column, v);
                queryWrapper.ge("ge".equals(symbol) && ObjectUtil.isNotEmpty(v), column, v);
                queryWrapper.lt("lt".equals(symbol) && ObjectUtil.isNotEmpty(v), column, v);
                queryWrapper.le("le".equals(symbol) && ObjectUtil.isNotEmpty(v), column, v);
                queryWrapper.like("like".equals(symbol) && ObjectUtil.isNotEmpty(v), column, v);
                queryWrapper.eq("".equals(symbol) && ObjectUtil.isNotEmpty(v), column, v);
            }
        });
        return queryWrapper;
    }

    /**
     * 查询过滤字段
     *
     * @param queryWrapper
     * @param selectFieldList
     */
    protected void selectFiledFilter(QueryWrapper queryWrapper, List<String> selectFieldList) {
        Predicate<TableFieldInfo> predicate;
        // select自定义查询字段或者Vo中的字段
        if (CollUtil.isNotEmpty(selectFieldList)) {
            predicate = (info) -> selectFieldList.contains(info.getProperty()) && selectFields.contains(info.getProperty());
        } else {
            predicate = (info) -> selectFields.contains(info.getProperty());
        }
        queryWrapper.select(this.getService().getEntityClass(), predicate);
    }

}
