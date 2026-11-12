package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.CreateFaultRuleCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands.UpdateFaultRuleCommand;
import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.FaultBand;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Cause-effect rule: what a deviation of a parameter in a band probably means and what to do.
 */
@Getter
public class FaultRule extends AbstractDomainAggregateRoot<FaultRule> {

    @Setter
    private Long id;
    private final OrganizationId organizationId;
    private final String parameter;
    private final FaultBand band;
    private String probableCause;
    private String recommendedAction;
    private int cooldownSeconds;

    public FaultRule(Long id, OrganizationId organizationId, String parameter, FaultBand band, String probableCause, String recommendedAction, Integer cooldownSeconds) {
        if (parameter == null || !parameter.matches("^[a-z][a-z0-9_]{2,39}$")) throw new IllegalArgumentException("Parameter must be snake_case, e.g. fuel_gas_flow");
        this.id = id;
        this.organizationId = Objects.requireNonNull(organizationId, "organizationId must not be null");
        this.parameter = parameter;
        this.band = Objects.requireNonNull(band, "band must not be null");
        update(probableCause, recommendedAction, cooldownSeconds);
    }

    public FaultRule(CreateFaultRuleCommand c) {
        this(null, new OrganizationId(c.organizationId()), c.parameter(), FaultBand.fromString(c.band()), c.probableCause(), c.recommendedAction(), c.cooldownSeconds());
    }

    public static FaultRule reconstitute(Long id, OrganizationId organizationId, String parameter, FaultBand band, String probableCause, String recommendedAction, int cooldownSeconds) {
        return new FaultRule(id, organizationId, parameter, band, probableCause, recommendedAction, cooldownSeconds);
    }

    public void update(UpdateFaultRuleCommand c) {
        update(c.probableCause(), c.recommendedAction(), c.cooldownSeconds());
    }

    private void update(String probableCause, String recommendedAction, Integer cooldownSeconds) {
        if (probableCause == null || probableCause.isBlank()) throw new IllegalArgumentException("Probable cause must not be blank");
        if (recommendedAction == null || recommendedAction.isBlank()) throw new IllegalArgumentException("Recommended action must not be blank");
        if (cooldownSeconds != null && cooldownSeconds < 0) throw new IllegalArgumentException("Cooldown must be zero or positive");
        this.probableCause = probableCause.trim();
        this.recommendedAction = recommendedAction.trim();
        this.cooldownSeconds = cooldownSeconds == null ? 60 : cooldownSeconds;
    }

    public boolean isOwnedBy(Long organizationId) { return this.organizationId.organizationId().equals(organizationId); }

    public Long getOrganizationIdValue() { return organizationId.organizationId(); }
}
