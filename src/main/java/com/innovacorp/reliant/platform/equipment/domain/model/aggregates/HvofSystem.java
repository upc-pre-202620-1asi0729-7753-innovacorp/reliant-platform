package com.innovacorp.reliant.platform.equipment.domain.model.aggregates;

import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterHvofSystemCommand;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.FuelType;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemStatus;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"organization_id", "code"}, name = HvofSystem.ORGANIZATION_CODE_UNIQUE_CONSTRAINT)
})
public class HvofSystem extends AuditableAbstractAggregateRoot<HvofSystem> {
    public static final String ORGANIZATION_CODE_UNIQUE_CONSTRAINT = "uk_hvof_system_organization_id_code";

    @Embedded
    @AttributeOverride(name = "organizationId", column = @Column(name = "organization_id", nullable = false))
    private OrganizationId organizationId;

    @Embedded
    @AttributeOverride(name = "code", column = @Column(name = "code", nullable = false, length = 32))
    private HvofSystemCode code;

    @Column(nullable = false, length = 64)
    private String serialNumber;

    @Column(nullable = false, length = 80)
    private String systemManufacturer;

    @Column(nullable = false, length = 120)
    private String systemModel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HvofSystemStatus status;

    protected HvofSystem() {}

    public HvofSystem(RegisterHvofSystemCommand command) {
        this.organizationId = command.organizationId();
        this.code = command.code();
        this.serialNumber = command.serialNumber();
        this.systemManufacturer = command.systemManufacturer();
        this.systemModel = command.systemModel();
        this.fuelType = command.fuelType();
        this.status = HvofSystemStatus.ACTIVE;
    }

    public Long getOrganizationIdValue() { return organizationId.organizationId(); }
    public String getCodeValue() { return code.code(); }
}