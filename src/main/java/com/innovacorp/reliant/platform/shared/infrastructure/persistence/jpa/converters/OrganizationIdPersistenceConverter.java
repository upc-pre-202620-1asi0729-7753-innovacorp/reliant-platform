package com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts OrganizationId between the domain model and a BIGINT column.
 */
@Converter(autoApply = false)
public class OrganizationIdPersistenceConverter implements AttributeConverter<OrganizationId, Long> {

    @Override
    public Long convertToDatabaseColumn(OrganizationId attribute) {
        return attribute == null ? null : attribute.organizationId();
    }

    @Override
    public OrganizationId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new OrganizationId(dbData);
    }
}