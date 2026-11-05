package com.innovacorp.reliant.platform.shared.infrastructure.events;

import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publishes and clears domain events registered on AbstractDomainAggregateRoot instances.
 * Every repository adapter calls publishAndClearEvents(saved) once, right after save.
 */
@Component
@NullMarked
public class DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public DomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public <T extends AbstractDomainAggregateRoot<T>> void publishAndClearEvents(@Nullable T aggregate) {
        if (aggregate == null) {
            return;
        }
        aggregate.domainEvents().forEach(applicationEventPublisher::publishEvent);
        aggregate.clearDomainEvents();
    }
}