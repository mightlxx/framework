package com.mightlin.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectUtil {

    public static List<String> getFieldNames(Class clazz) {
        return Arrays.stream(cn.hutool.core.util.ReflectUtil.getFields(clazz)).map(field -> field.getName())
                .collect(Collectors.toList());
    }
}
