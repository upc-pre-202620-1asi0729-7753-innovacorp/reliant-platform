package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.application.commandservices.HvofSystemCommandFailure;
import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public class ResponseEntityFromHvofSystemCommandResultAssembler {
    public static ResponseEntity<?> toResponseEntityFromResult(Result<HvofSystem, HvofSystemCommandFailure> result, MessageSource messageSource) {
        return result.fold(
                system -> new ResponseEntity<>(HvofSystemResourceFromEntityAssembler.toResourceFromEntity(system), HttpStatus.CREATED),
                failure -> {
                    var status = switch (failure) { case HvofSystemCommandFailure.Duplicate d -> HttpStatus.CONFLICT; };
                    var message = messageSource.getMessage(failure.messageKey(), null, failure.messageKey(), LocaleContextHolder.getLocale());
                    return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, message));
                });
    }
}