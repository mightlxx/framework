package com.mightlin.common.lock;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DefaultDomainLock implements DomainLock{

    private static final ConcurrentHashMap<Long, ReentrantLock> LOCK_MAP = new ConcurrentHashMap<>();

    @Override
    public void lock(Long key) {
        ReentrantLock reentrantLock = LOCK_MAP.get(key);
        if (reentrantLock == null) {
            reentrantLock = new ReentrantLock(true);
            ReentrantLock existed = LOCK_MAP.putIfAbsent(key, reentrantLock);
            if (existed != null) {
                reentrantLock = existed;
            }
        }
        reentrantLock.lock();
    }

    @Override
    public void tryLock(Long key) {
        ReentrantLock reentrantLock = LOCK_MAP.get(key);
        if (reentrantLock == null) {
            reentrantLock = new ReentrantLock(true);
            ReentrantLock existed = LOCK_MAP.putIfAbsent(key, reentrantLock);
            if (existed != null) {
                reentrantLock = existed;
            }
        }
        reentrantLock.tryLock();

    }

    @Override
    public void tryLock(Long key,long tryMilliSeconds) throws InterruptedException {
        ReentrantLock reentrantLock = LOCK_MAP.get(key);
        if (reentrantLock == null) {
            reentrantLock = new ReentrantLock(true);
            ReentrantLock existed = LOCK_MAP.putIfAbsent(key, reentrantLock);
            if (existed != null) {
                reentrantLock = existed;
            }
        }
        reentrantLock.tryLock(tryMilliSeconds, TimeUnit.MILLISECONDS);
    }

    @Override
    public void unlock(Long key) {
        ReentrantLock reentrantLock = LOCK_MAP.get(key);
        if (reentrantLock == null){
            reentrantLock.unlock();
        }
    }
}
