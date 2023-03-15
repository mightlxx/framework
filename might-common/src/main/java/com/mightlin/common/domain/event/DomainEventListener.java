package com.mightlin.common.domain.event;

import org.springframework.context.ApplicationListener;

public interface DomainEventListener<E extends DomainEvent> extends ApplicationListener<E> {

    void listener(E event);

    default void onApplicationEvent(E event) {
        listener(event);
    }
}
