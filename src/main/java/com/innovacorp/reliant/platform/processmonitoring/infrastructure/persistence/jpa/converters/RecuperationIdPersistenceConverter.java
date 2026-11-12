package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecuperationId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class RecuperationIdPersistenceConverter implements AttributeConverter<RecuperationId, Long> {

    @Override
    public Long convertToDatabaseColumn(RecuperationId attribute) {
        return attribute == null ? null : attribute.recuperationId();
    }

    @Override
    public RecuperationId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new RecuperationId(dbData);
    }
}