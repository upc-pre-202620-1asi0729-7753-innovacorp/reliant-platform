package com.innovacorp.reliant.platform.processmonitoring.application.internal.commandservices;

import com.innovacorp.reliant.platform.processmonitoring.application.commandservices.SpraySessionCommandService;
import com.innovacorp.reliant.platform.processmonitoring.application.internal.outboundservices.acl.ExternalEquipmentService;
import com.innovacorp.reliant.platform.processmonitoring.application.internal.outboundservices.acl.ExternalTraceabilityService;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.commands.*;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.SprayPass;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;
import com.innovacorp.reliant.platform.processmonitoring.domain.repositories.ProcessReadingRepository;
import com.innovacorp.reliant.platform.processmonitoring.domain.repositories.SpraySessionRepository;
import com.innovacorp.reliant.platform.shared.application.context.CurrentUserProvider;
import com.innovacorp.reliant.platform.shared.application.result.ApplicationError;
import com.innovacorp.reliant.platform.shared.application.result.Result;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.function.Consumer;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class SpraySessionCommandServiceImpl implements SpraySessionCommandService {

    private final SpraySessionRepository sessionRepository;
    private final ProcessReadingRepository readingRepository;
    private final ExternalEquipmentService externalEquipmentService;
    private final ExternalTraceabilityService externalTraceabilityService;
    private final CurrentUserProvider currentUserProvider;

    public SpraySessionCommandServiceImpl(SpraySessionRepository sessionRepository,
                                          ProcessReadingRepository readingRepository,
                                          ExternalEquipmentService externalEquipmentService,
                                          ExternalTraceabilityService externalTraceabilityService,
                                          CurrentUserProvider currentUserProvider) {
        this.sessionRepository = sessionRepository;
        this.readingRepository = readingRepository;
        this.externalEquipmentService = externalEquipmentService;
        this.externalTraceabilityService = externalTraceabilityService;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public Result<SpraySession, ApplicationError> handle(StartSpraySessionCommand command) {
        var currentUser = currentUserProvider.current();
        var systemOrganizationId = externalEquipmentService
                .fetchHvofSystemOrganizationId(command.hvofSystemId());

        if (systemOrganizationId == 0L) {
            return Result.failure(ApplicationError.notFound(
                    "hvof-system", String.valueOf(command.hvofSystemId())));
        }
        if (!systemOrganizationId.equals(currentUser.organizationId())) {
            return Result.failure(new ApplicationError(
                    "ACCESS_FORBIDDEN", "Access forbidden",
                    "The HVOF system belongs to another organization"));
        }

        var recuperationOrganizationId = externalTraceabilityService
                .fetchRecuperationSupplierOrganizationId(command.recuperationId());
        if (recuperationOrganizationId == 0L
                || !recuperationOrganizationId.equals(currentUser.organizationId())) {
            return Result.failure(ApplicationError.notFound(
                    "recuperation", String.valueOf(command.recuperationId())));
        }

        var recipeBands = externalEquipmentService.fetchActiveRecipeBands(
                command.hvofSystemId(), command.recipeNumber());
        if (recipeBands.isEmpty()) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "active-recipe",
                    "Recipe " + command.recipeNumber() + " is not ACTIVE for this HVOF system"));
        }

        SpraySession session;
        try {
            session = new SpraySession(
                    new OrganizationId(currentUser.organizationId()),
                    new HvofSystemId(command.hvofSystemId()),
                    new RecuperationId(command.recuperationId()),
                    currentUser.userId(), command.recipeNumber(), command.timeZone(), recipeBands);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("SpraySession", e.getMessage()));
        }

        var savedSession = sessionRepository.save(session);
        if (!externalTraceabilityService.linkSessionToRecuperation(
                command.recuperationId(), savedSession.getId())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.failure(ApplicationError.businessRuleViolation(
                    "recuperation-link", "The spray session could not be linked to the recuperation"));
        }

        return Result.success(savedSession);
    }

    @Override
    public Result<SpraySession, ApplicationError> handle(CompleteSpraySessionCommand command) {
        return transition(command.spraySessionId(), SpraySession::complete);
    }

    @Override
    public Result<SpraySession, ApplicationError> handle(AbortSpraySessionCommand command) {
        return transition(command.spraySessionId(), session -> session.abort(command.reason()));
    }

    @Override
    public Result<List<ProcessReading>, ApplicationError> handle(IngestProcessReadingsCommand command) {
        var owned = ownedActiveSession(command.spraySessionId());
        if (owned instanceof Result.Failure<SpraySession, ApplicationError> failure) {
            return Result.failure(failure.error());
        }
        var session = ((Result.Success<SpraySession, ApplicationError>) owned).value();

        List<ProcessReading> readings;
        try {
            readings = command.readings().stream()
                    .map(reading -> session.classify(
                            reading.epochMillis(),
                            reading.plcClockOffsetMillis() == null ? 0L : reading.plcClockOffsetMillis(),
                            reading.tagPath(), reading.parameter(), reading.subsystemId(),
                            reading.partId(), reading.value()))
                    .toList();
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("ProcessReading", e.getMessage()));
        }

        var savedReadings = readingRepository.saveAll(readings);
        sessionRepository.save(session);
        return Result.success(savedReadings);
    }

    @Override
    public Result<SprayPass, ApplicationError> handle(RegisterSprayPassCommand command) {
        var owned = ownedActiveSession(command.spraySessionId());
        if (owned instanceof Result.Failure<SpraySession, ApplicationError> failure) {
            return Result.failure(failure.error());
        }
        var session = ((Result.Success<SpraySession, ApplicationError>) owned).value();

        SprayPass pass;
        try {
            pass = session.registerPass(command.passNumber(), command.startedAt(), command.endedAt());
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.conflict("spray-pass", String.valueOf(command.passNumber())));
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("SprayPass", e.getMessage()));
        }

        var savedPass = sessionRepository.save(session).getPasses().stream()
                .filter(existing -> existing.getPassNumber() == pass.getPassNumber())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Pass not found after save"));
        return Result.success(savedPass);
    }

    private Result<SpraySession, ApplicationError> transition(
            Long spraySessionId, Consumer<SpraySession> action) {
        var owned = ownedActiveSession(spraySessionId);
        if (owned.isFailure()) {
            return owned;
        }
        var session = ((Result.Success<SpraySession, ApplicationError>) owned).value();

        try {
            action.accept(session);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("SpraySession", e.getMessage()));
        }

        return Result.success(sessionRepository.save(session));
    }

    private Result<SpraySession, ApplicationError> ownedActiveSession(Long spraySessionId) {
        var organizationId = currentUserProvider.current().organizationId();
        var session = sessionRepository.findById(spraySessionId)
                .filter(existing -> existing.getOrganizationIdValue().equals(organizationId));

        if (session.isEmpty()) {
            return Result.failure(ApplicationError.notFound(
                    "spray-session", String.valueOf(spraySessionId)));
        }
        if (!session.get().isActive()) {
            return Result.failure(ApplicationError.businessRuleViolation(
                    "session-status", "The spray session is not active"));
        }

        return Result.success(session.get());
    }
}