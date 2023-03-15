package com.mightlin.common.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *  id
 * @param <T>
 */
@Data
public class IdModel<T> {

    /**
     * id
     */
    @NotNull
    private T id;
}
