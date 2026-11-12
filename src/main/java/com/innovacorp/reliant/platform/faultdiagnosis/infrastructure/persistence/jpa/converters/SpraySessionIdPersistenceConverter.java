package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.SpraySessionId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SpraySessionIdPersistenceConverter implements AttributeConverter<SpraySessionId, Long> {
    @Override
    public Long convertToDatabaseColumn(SpraySessionId attribute) { return attribute == null ? null : attribute.spraySessionId(); }

    @Override
    public SpraySessionId convertToEntityAttribute(Long dbData) { return dbData == null ? null : new SpraySessionId(dbData); }
}
