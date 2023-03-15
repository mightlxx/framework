package com.mightlin.common.model;

import lombok.Data;

import java.util.List;

/**
 * id列表
 * @param <T>
 */
@Data
public class IdListModel<T> {

    /**
     * id集合
     */
    private List<T> idList;
}
