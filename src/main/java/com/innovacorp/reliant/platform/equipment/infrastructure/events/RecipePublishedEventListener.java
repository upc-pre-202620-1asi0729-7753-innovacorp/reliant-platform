package com.innovacorp.reliant.platform.equipment.infrastructure.events;

import com.innovacorp.reliant.platform.equipment.domain.model.events.RecipePublishedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RecipePublishedEventListener {

    private static final Logger log =
            LoggerFactory.getLogger(RecipePublishedEventListener.class);

    @EventListener
    public void onRecipePublished(RecipePublishedEvent event) {
        log.info("Recipe published: {}", event);
    }
}