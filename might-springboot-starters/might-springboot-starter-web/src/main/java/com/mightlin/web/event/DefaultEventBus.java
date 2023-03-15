package com.mightlin.web.event;

import com.mightlin.common.domain.event.DomainEvent;
import com.mightlin.common.domain.event.DomainEventBus;
import com.mightlin.common.domain.event.DomainEventListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 事件总线单体简单实现
 */
@Component
public class DefaultEventBus implements DomainEventBus, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void send(DomainEvent event) {
        applicationContext.publishEvent(event);
    }

    @Override
    public void register(DomainEventListener handler) {
//         applicationContext.addApplicationListener(handler);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
