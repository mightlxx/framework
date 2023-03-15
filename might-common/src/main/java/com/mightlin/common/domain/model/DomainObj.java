package com.mightlin.common.domain.model;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mightlin.common.lock.DomainLock;

public interface DomainObj<S extends IService> {

    Long getId();

    void setId(Long id);

    /**
     * 锁
     */
    default void lock() {
        DomainLock domainLock = SpringUtil.getBean(DomainLock.class);
        domainLock.lock(getId());
    }

    /**
     * 解锁
     */
    default void unlock() {
        DomainLock domainLock = SpringUtil.getBean(DomainLock.class);
        domainLock.unlock(getId());
    }

}
