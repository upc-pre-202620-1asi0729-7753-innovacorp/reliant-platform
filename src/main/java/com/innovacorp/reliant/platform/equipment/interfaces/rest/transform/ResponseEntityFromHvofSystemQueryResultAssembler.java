package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.HvofSystemResource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class ResponseEntityFromHvofSystemQueryResultAssembler {
    public static ResponseEntity<?> toResponseEntityFromOptional(Optional<HvofSystem> result, Long id, MessageSource messageSource) {
        return result.<ResponseEntity<?>>map(system -> ResponseEntity.ok(HvofSystemResourceFromEntityAssembler.toResourceFromEntity(system)))
                .orElseGet(() -> {
                    var message = messageSource.getMessage("hvof.system.error.notFound", new Object[]{id}, LocaleContextHolder.getLocale());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message));
                });
    }

    public static ResponseEntity<List<HvofSystemResource>> toResponseEntityFromList(List<HvofSystem> systems) {
        return ResponseEntity.ok(systems.stream().map(HvofSystemResourceFromEntityAssembler::toResourceFromEntity).toList());
    }
}