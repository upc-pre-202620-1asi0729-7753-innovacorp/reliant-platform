package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.events.FaultCaseOpenedEvent;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.*;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

/**
 * A deviation that matched a FaultRule. Lifecycle: OPEN -> ACKNOWLEDGED -> RESOLVED.
 */
@Getter
public class FaultCase extends AbstractDomainAggregateRoot<FaultCase> {

    @Setter
    private Long id;
    private final OrganizationId organizationId;
    private final SpraySessionId spraySessionId;
    private final HvofSystemId hvofSystemId;
    private final String parameter;
    private final FaultBand band;
    private final double value;
    private final long epochMillis;
    private final String probableCause;
    private final String recommendedAction;
    private FaultCaseStatus status;
    private UserId acknowledgedBy;
    private Date acknowledgedAt;
    private UserId resolvedBy;
    private Date resolvedAt;
    private String resolutionNotes;

    private FaultCase(Long id, OrganizationId organizationId, SpraySessionId spraySessionId, HvofSystemId hvofSystemId, String parameter, FaultBand band,
                      double value, long epochMillis, String probableCause, String recommendedAction, FaultCaseStatus status,
                      UserId acknowledgedBy, Date acknowledgedAt, UserId resolvedBy, Date resolvedAt, String resolutionNotes) {
        this.id = id;
        this.organizationId = Objects.requireNonNull(organizationId);
        this.spraySessionId = Objects.requireNonNull(spraySessionId);
        this.hvofSystemId = Objects.requireNonNull(hvofSystemId);
        this.parameter = Objects.requireNonNull(parameter);
        this.band = Objects.requireNonNull(band);
        this.value = value;
        this.epochMillis = epochMillis;
        this.probableCause = Objects.requireNonNull(probableCause);
        this.recommendedAction = Objects.requireNonNull(recommendedAction);
        this.status = Objects.requireNonNull(status);
        this.acknowledgedBy = acknowledgedBy;
        this.acknowledgedAt = acknowledgedAt;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = resolvedAt;
        this.resolutionNotes = resolutionNotes;
    }

    /** Opens a case from a deviation and the rule that matched it. */
    public static FaultCase open(OrganizationId organizationId, SpraySessionId spraySessionId, HvofSystemId hvofSystemId, String parameter, FaultBand band,
                                 double value, long epochMillis, FaultRule rule) {
        return new FaultCase(null, organizationId, spraySessionId, hvofSystemId, parameter, band, value, epochMillis,
                rule.getProbableCause(), rule.getRecommendedAction(), FaultCaseStatus.OPEN, null, null, null, null, null);
    }

    public static FaultCase reconstitute(Long id, OrganizationId organizationId, SpraySessionId spraySessionId, HvofSystemId hvofSystemId, String parameter, FaultBand band,
                                         double value, long epochMillis, String probableCause, String recommendedAction, FaultCaseStatus status,
                                         UserId acknowledgedBy, Date acknowledgedAt, UserId resolvedBy, Date resolvedAt, String resolutionNotes) {
        return new FaultCase(id, organizationId, spraySessionId, hvofSystemId, parameter, band, value, epochMillis, probableCause, recommendedAction, status,
                acknowledgedBy, acknowledgedAt, resolvedBy, resolvedAt, resolutionNotes);
    }

    public void onOpened() {
        registerEvent(new FaultCaseOpenedEvent(id, organizationId.organizationId(), spraySessionId.spraySessionId(), parameter, band.name(), probableCause));
    }

    public void acknowledge(UserId userId) {
        if (status != FaultCaseStatus.OPEN) throw new IllegalStateException("Only OPEN cases can be acknowledged (current: " + status + ")");
        this.acknowledgedBy = Objects.requireNonNull(userId);
        this.acknowledgedAt = new Date();
        this.status = FaultCaseStatus.ACKNOWLEDGED;
    }

    public void resolve(UserId userId, String notes) {
        if (status == FaultCaseStatus.RESOLVED) throw new IllegalStateException("Case is already RESOLVED");
        if (status == FaultCaseStatus.OPEN) acknowledge(userId);
        this.resolvedBy = Objects.requireNonNull(userId);
        this.resolvedAt = new Date();
        this.resolutionNotes = notes == null ? null : notes.trim();
        this.status = FaultCaseStatus.RESOLVED;
    }

    public boolean isOwnedBy(Long organizationId) { return this.organizationId.organizationId().equals(organizationId); }

    public Long getOrganizationIdValue() { return organizationId.organizationId(); }

    public Long getSpraySessionIdValue() { return spraySessionId.spraySessionId(); }

    public Long getHvofSystemIdValue() { return hvofSystemId.hvofSystemId(); }

    public Long getAcknowledgedByValue() { return acknowledgedBy == null ? null : acknowledgedBy.userId(); }

    public Long getResolvedByValue() { return resolvedBy == null ? null : resolvedBy.userId(); }
}
