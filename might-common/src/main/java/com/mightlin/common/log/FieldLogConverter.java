package com.mightlin.common.log;

public interface FieldLogConverter<T> {

    String convert(T fieldValue);
}
