package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.HvofSystemId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class HvofSystemIdPersistenceConverter implements AttributeConverter<HvofSystemId, Long> {
    @Override
    public Long convertToDatabaseColumn(HvofSystemId attribute) { return attribute == null ? null : attribute.hvofSystemId(); }

    @Override
    public HvofSystemId convertToEntityAttribute(Long dbData) { return dbData == null ? null : new HvofSystemId(dbData); }
}
