package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.HvofSystemCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converts HvofSystemCode between the domain model and a VARCHAR column.
 */
@Converter(autoApply = false)
public class HvofSystemCodePersistenceConverter implements AttributeConverter<HvofSystemCode, String> {

    @Override
    public String convertToDatabaseColumn(HvofSystemCode attribute) {
        return attribute == null ? null : attribute.code();
    }

    @Override
    public HvofSystemCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new HvofSystemCode(dbData);
    }
}