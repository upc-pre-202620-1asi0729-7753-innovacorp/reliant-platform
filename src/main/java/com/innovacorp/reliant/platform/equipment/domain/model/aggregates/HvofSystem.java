package com.innovacorp.reliant.platform.equipment.domain.model.aggregates;

import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.events.HvofSystemRegisteredEvent;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.FuelType;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemStatus;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * HVOF System aggregate root. No persistence annotation lives here — see HvofSystemPersistenceEntity.
 */
@Getter
public class HvofSystem extends AbstractDomainAggregateRoot<HvofSystem> {

    @Setter
    private Long id;
    private final OrganizationId organizationId;
    private final HvofSystemCode code;
    private final String serialNumber;
    private final String systemManufacturer;
    private final String systemModel;
    private final FuelType fuelType;
    private HvofSystemStatus status;

    public HvofSystem(Long id, OrganizationId organizationId, HvofSystemCode code, String serialNumber,
                      String systemManufacturer, String systemModel, FuelType fuelType, HvofSystemStatus status) {
        this.id = id;
        this.organizationId = Objects.requireNonNull(organizationId, "organizationId must not be null");
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.serialNumber = requireText(serialNumber, "Serial number");
        this.systemManufacturer = requireText(systemManufacturer, "System manufacturer");
        this.systemModel = requireText(systemModel, "System model");
        this.fuelType = Objects.requireNonNull(fuelType, "fuelType must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public HvofSystem(OrganizationId organizationId, HvofSystemCode code, String serialNumber,
                      String systemManufacturer, String systemModel, FuelType fuelType) {
        this(null, organizationId, code, serialNumber, systemManufacturer, systemModel, fuelType, HvofSystemStatus.ACTIVE);
    }

    public HvofSystem(RegisterHvofSystemCommand command) {
        this(new OrganizationId(command.organizationId()),
                new HvofSystemCode(command.code()),
                command.serialNumber(),
                command.systemManufacturer(),
                command.systemModel(),
                FuelType.fromString(command.fuelType()));
    }

    public static HvofSystem reconstitute(Long id, OrganizationId organizationId, HvofSystemCode code, String serialNumber,
                                          String systemManufacturer, String systemModel, FuelType fuelType, HvofSystemStatus status) {
        return new HvofSystem(id, organizationId, code, serialNumber, systemManufacturer, systemModel, fuelType, status);
    }

    public Long getOrganizationIdValue() { return organizationId.organizationId(); }

    public String getCodeValue() { return code.code(); }

    public void changeStatus(HvofSystemStatus status) { this.status = Objects.requireNonNull(status); }

    /** Called by the repository adapter once the JPA identity exists. */
    public void onCreated() { registerDomainEvent(HvofSystemRegisteredEvent.from(this)); }

    private static String requireText(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " must not be null or blank");
        return value.trim();
    }
}