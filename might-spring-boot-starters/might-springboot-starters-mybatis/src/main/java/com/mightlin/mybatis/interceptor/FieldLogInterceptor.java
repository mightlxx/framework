package com.mightlin.mybatis.interceptor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.mightlin.common.constant.SysConstant;
import com.mightlin.common.dict.DataDictService;
import com.mightlin.common.log.FieldLogConverter;
import com.mightlin.common.log.OperateLogField;
import com.mightlin.common.log.OptLogFieldContentThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 字段内容修改日志拦截器
 */
@Slf4j
public class FieldLogInterceptor implements InnerInterceptor {

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        Object entity = parameter;
        if (parameter instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameter;
            entity = paramMap.get(Constants.ENTITY);
        }
        OperateLogField entityAnnotation = entity.getClass().getAnnotation(OperateLogField.class);
        if (entityAnnotation == null) {
            return;
        }
        // 字段更新日志
        this.doFieldLog(entity);
    }

    private void doFieldLog(Object entity) {
        StringBuilder sb = new StringBuilder();
        Stream.of(ReflectUtil.getFields(entity.getClass())).forEach(field -> {
            OperateLogField annotation = field.getAnnotation(OperateLogField.class);
            Object fieldValue = ReflectUtil.getFieldValue(entity, field);
            if (annotation != null && fieldValue != null) {
                if (sb.length() > 0) {
                    sb.append(SysConstant.LINE_SEPARATOR);
                }
                sb.append(annotation.name());
                sb.append(":");
                sb.append(this.getValueStr(annotation, fieldValue));

            }
        });
        OptLogFieldContentThreadLocal.append(sb);
    }

    @SuppressWarnings("unchecked")
    private String getValueStr(OperateLogField annotation, Object fieldValue) {
        try {
            // 字典值转换
            if (StrUtil.isNotBlank(annotation.dictCode())) {
                DataDictService dataDictService = SpringUtil.getBean(DataDictService.class);
                Map<String, String> dictItemMap = dataDictService.getDictItemMap(annotation.dictCode());
                return dictItemMap.get(String.valueOf(fieldValue.toString()));
            }
            // 自定义转换器
            if (FieldLogConverter.class == annotation.converter()) {
                return StrUtil.toString(fieldValue);
            } else {
                FieldLogConverter converter = SpringUtil.getBean(annotation.converter());
                return converter.convert(fieldValue);
            }
        } catch (Exception e) {
            log.warn("实体字段日志值转换异常：", e);
        }
        return "";
    }
}
