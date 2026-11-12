package com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.SprayPass;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.events.OutOfBandReadingDetectedEvent;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.events.SpraySessionCompletedEvent;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.events.SpraySessionStartedEvent;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.Band;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.HvofSystemId;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecipeBand;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.SpraySessionStatus;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
public class SpraySession extends AbstractDomainAggregateRoot<SpraySession> {

    @Setter
    private Long id;
    private final OrganizationId organizationId;
    private final HvofSystemId hvofSystemId;
    private final RecuperationId recuperationId;
    private final Long operatorId;
    private final Integer recipeNumber;
    private final Instant startedAt;
    private Instant endedAt;
    private final String timeZone;
    private SpraySessionStatus status;
    private String abortReason;
    private final List<RecipeBand> recipeBands;
    private final List<SprayPass> passes;

    public SpraySession(Long id, OrganizationId organizationId, HvofSystemId hvofSystemId,
                        RecuperationId recuperationId, Long operatorId, Integer recipeNumber,
                        Instant startedAt, Instant endedAt, String timeZone, SpraySessionStatus status,
                        String abortReason, List<RecipeBand> recipeBands, List<SprayPass> passes) {
        if (operatorId == null || operatorId <= 0) {
            throw new IllegalArgumentException("Operator id must be positive");
        }
        if (recipeNumber == null || recipeNumber <= 0) {
            throw new IllegalArgumentException("Recipe number must be positive");
        }
        if (recipeBands == null || recipeBands.isEmpty()) {
            throw new IllegalArgumentException("A session needs the bands of an active recipe");
        }

        this.id = id;
        this.organizationId = Objects.requireNonNull(organizationId, "organizationId must not be null");
        this.hvofSystemId = Objects.requireNonNull(hvofSystemId, "hvofSystemId must not be null");
        this.recuperationId = Objects.requireNonNull(recuperationId, "recuperationId must not be null");
        this.operatorId = operatorId;
        this.recipeNumber = recipeNumber;
        this.startedAt = startedAt == null ? Instant.now() : startedAt;
        this.endedAt = endedAt;
        this.timeZone = timeZone == null || timeZone.isBlank() ? "America/Lima" : timeZone.trim();
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.abortReason = abortReason;
        this.recipeBands = List.copyOf(recipeBands);
        this.passes = new ArrayList<>(passes == null ? List.of() : passes);
    }

    public SpraySession(OrganizationId organizationId, HvofSystemId hvofSystemId,
                        RecuperationId recuperationId, Long operatorId, Integer recipeNumber,
                        String timeZone, List<RecipeBand> recipeBands) {
        this(null, organizationId, hvofSystemId, recuperationId, operatorId, recipeNumber,
                Instant.now(), null, timeZone, SpraySessionStatus.ACTIVE, null, recipeBands, List.of());
    }

    public static SpraySession reconstitute(Long id, OrganizationId organizationId, HvofSystemId hvofSystemId,
                                            RecuperationId recuperationId, Long operatorId, Integer recipeNumber,
                                            Instant startedAt, Instant endedAt, String timeZone,
                                            SpraySessionStatus status, String abortReason,
                                            List<RecipeBand> recipeBands, List<SprayPass> passes) {
        return new SpraySession(id, organizationId, hvofSystemId, recuperationId, operatorId, recipeNumber,
                startedAt, endedAt, timeZone, status, abortReason, recipeBands, passes);
    }

    public boolean isActive() {
        return status == SpraySessionStatus.ACTIVE;
    }

    public Optional<RecipeBand> bandFor(String parameter) {
        return recipeBands.stream()
                .filter(band -> band.parameter().equals(parameter))
                .findFirst();
    }

    public ProcessReading classify(long epochMillis, long plcClockOffsetMillis, String tagPath,
                                   String parameter, Long subsystemId, Long partId, double value) {
        if (!isActive()) {
            throw new IllegalStateException("Readings can only be ingested into an active session");
        }
        if (parameter == null || parameter.isBlank()) {
            throw new IllegalArgumentException("parameter must not be blank");
        }

        String parameterName = parameter.trim();
        var recipeBand = bandFor(parameterName);
        var classification = recipeBand.map(band -> band.classify(value)).orElse(Band.NOMINAL);
        var reading = new ProcessReading(
                id, epochMillis, plcClockOffsetMillis, tagPath, parameterName, subsystemId, partId, value,
                recipeBand.map(RecipeBand::unitSymbol).orElse(""),
                recipeBand.map(RecipeBand::unitCategory).orElse(""),
                classification, false, recipeBand.isEmpty());

        if (classification.isDeviation()) {
            registerDomainEvent(OutOfBandReadingDetectedEvent.from(
                    reading, getHvofSystemIdValue(), getOrganizationIdValue()));
        }
        return reading;
    }

    public SprayPass registerPass(int passNumber, Instant startedAt, Instant endedAt) {
        if (!isActive()) {
            throw new IllegalStateException("Passes can only be registered in an active session");
        }
        if (passes.stream().anyMatch(pass -> pass.getPassNumber() == passNumber)) {
            throw new IllegalStateException("Pass number " + passNumber + " already exists in this session");
        }

        var pass = new SprayPass(passNumber, startedAt, endedAt);
        passes.add(pass);
        return pass;
    }

    public void complete() {
        if (!isActive()) {
            throw new IllegalStateException("Only active sessions can be completed");
        }

        status = SpraySessionStatus.COMPLETED;
        endedAt = Instant.now();
        registerDomainEvent(SpraySessionCompletedEvent.from(this));
    }

    public void abort(String reason) {
        if (!isActive()) {
            throw new IllegalStateException("Only active sessions can be aborted");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Abort reason must not be blank");
        }

        status = SpraySessionStatus.ABORTED;
        abortReason = reason.trim();
        endedAt = Instant.now();
        registerDomainEvent(SpraySessionCompletedEvent.from(this));
    }

    public Long getOrganizationIdValue() {
        return organizationId.organizationId();
    }

    public Long getHvofSystemIdValue() {
        return hvofSystemId.hvofSystemId();
    }

    public Long getRecuperationIdValue() {
        return recuperationId.recuperationId();
    }

    public void onCreated() {
        registerDomainEvent(SpraySessionStartedEvent.from(this));
    }
}