package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.ComponentId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ComponentIdPersistenceConverter implements AttributeConverter<ComponentId, Long> {

    @Override
    public Long convertToDatabaseColumn(ComponentId attribute) {
        return attribute == null ? null : attribute.componentId();
    }

    @Override
    public ComponentId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new ComponentId(dbData);
    }
}