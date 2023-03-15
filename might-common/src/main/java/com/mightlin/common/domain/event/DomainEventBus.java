package com.mightlin.common.domain.event;

/**
 * 事件总线
 */
public interface DomainEventBus {

    void send(DomainEvent event);

    void register(DomainEventListener handler);

}
