package com.mightlin.common.lock;

public interface DomainLock {

    void lock(Long key);

    void tryLock(Long key);

    void tryLock(Long key, long tryMilliSeconds) throws InterruptedException;


    void unlock(Long key);
}
