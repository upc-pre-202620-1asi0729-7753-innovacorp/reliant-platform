package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.Ruc;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class RucPersistenceConverter implements AttributeConverter<Ruc, String> {

    @Override
    public String convertToDatabaseColumn(Ruc attribute) {
        return attribute == null ? null : attribute.ruc();
    }

    @Override
    public Ruc convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Ruc(dbData);
    }
}
