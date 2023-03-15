package com.mightlin.common.domain.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent<M> extends ApplicationEvent {

    private String eventId;
    private LocalDateTime eventTime;

    public M getMessage() {
        return (M) this.source;
    }

    public String getEventName() {
        return (String) this.source;
    }

    public DomainEvent(Object source) {
        super(source);
        eventId = UUID.randomUUID().toString();
        eventTime = LocalDateTime.now();
    }

    public abstract String key();

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime evenTime) {
        this.eventTime = evenTime;
    }
}
