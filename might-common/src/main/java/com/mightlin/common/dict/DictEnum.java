package com.mightlin.common.dict;

import cn.hutool.core.map.BiMap;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.mightlin.common.dict.DataDictService;
import com.mightlin.common.response.BusinessException;
import com.mightlin.common.util.GenericUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public interface DictEnum<T extends Serializable> extends IEnum<T> {

    String getDictCode();

    T getValue();

    String getLabel();

    default boolean equalsValue(Object value) {
        return getValue().equals(value);
    }

    /**
     * 根据label获取字典数据中的value值
     *
     * @param label
     * @return
     */
    default T getDictValueByLabel(String label) {
        return getDictBiMap().get(label);
    }

    /**
     * 根据value获取字典数据中的label值
     *
     * @param value
     * @return
     */
    default String getDictLabelByValue(T value) {
        return getDictBiMap().getInverse().get(value);
    }

    default BiMap<String, T> getDictBiMap() {
        Class typeClas = GenericUtil.getGenericClass(this.getClass(), "T");
        try {
            DataDictService dataDictService = SpringUtil.getBean(DataDictService.class);
            Map<String, String> dictItemMap = dataDictService.getDictItemMap(this.getDictCode());
            Map<String, T> dictMap = new HashMap<>();
            for (Map.Entry<String, String> entry : dictItemMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Method method = typeClas.getMethod("valueOf", typeClas);
                T targetValue = ReflectUtil.invokeStatic(method, value);
                dictMap.put(key, targetValue);
            }
            return new BiMap<>(dictMap);
        } catch (Exception e) {
            throw new BusinessException("获取字典发生异常成功，请检查！");
        }
    }

}
