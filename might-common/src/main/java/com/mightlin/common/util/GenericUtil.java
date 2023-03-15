package com.mightlin.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenericUtil {
    public static <T> Class<T> getGenericClass(Class clazz, String typeName) {
        if (ObjectUtil.isNull(clazz) || StrUtil.isBlank(typeName)) {
            throw new RuntimeException("输入泛型为空！！！");
        }
        return (Class<T>) TypeUtil.getTypeMap(clazz).entrySet().stream()
                .filter(t -> t.getKey().getTypeName().equals(typeName))
                .map(t -> t.getValue())
                .findAny().orElseThrow(() -> new RuntimeException("为找到泛型类！！！！！"));
    }
}
