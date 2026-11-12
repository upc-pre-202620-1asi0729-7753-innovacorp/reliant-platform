package com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.OrganizationStatus;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.OrganizationType;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.Ruc;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.converters.RucPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
public class OrganizationPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "legal_name", nullable = false, length = 150)
    private String legalName;

    @Convert(converter = RucPersistenceConverter.class)
    @Column(name = "ruc", nullable = false, unique = true, length = 11)
    private Ruc ruc;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type", nullable = false, length = 30)
    private OrganizationType organizationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrganizationStatus status;
}
