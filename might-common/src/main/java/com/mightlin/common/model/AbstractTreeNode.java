package com.mightlin.common.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTreeNode<T> {

    /**
     * 子节点列表
     */
    private List<T> children = new ArrayList();

    /**
     * id
     */
    public abstract Long getId();

    /**
     * 上级ID
     */
    public abstract Long getParentId();

    public List<T> getChildren() {
        return children;
    }
}
