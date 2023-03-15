package com.mightlin.common.domain.converter;

import org.mapstruct.MapperConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@MapperConfig(componentModel = "spring")
public interface ModelConverter<D, C, Q, V> {

    D cmdToEntity(C cmd);

    List<D> toPoList(List<C> cmdList);

    V entityToVo(D entity);

    D qoToEntity(Q qo);

    /**
     * 默认时间格式输出
     *
     * @param localDateTime
     * @return
     */
    default String localDateTimeToStr(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
