package com.innovacorp.reliant.platform.equipment.application.internal.eventhandlers;

import com.innovacorp.reliant.platform.equipment.domain.model.events.RecipePublishedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Internal application-layer handler for the {@link RecipePublishedEvent} domain event.
 *
 * <p>The event stays inside the Equipment bounded context: Process Monitoring never subscribes to it, it reads the
 * ACTIVE recipe bands through the Equipment ACL facade when a session starts. Reactions live in the application
 * layer (event handlers), never in infrastructure.</p>
 */
@Service("equipmentRecipePublishedEventHandler")
public class RecipePublishedEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecipePublishedEventHandler.class);

    @EventListener
    public void on(RecipePublishedEvent event) {
        LOGGER.info("Recipe {} published for HVOF system {}", event.recipeId(), event.hvofSystemId());
    }
}