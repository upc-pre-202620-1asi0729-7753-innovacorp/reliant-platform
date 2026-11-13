package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.CustomerId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class CustomerIdPersistenceConverter implements AttributeConverter<CustomerId, Long> {

    @Override
    public Long convertToDatabaseColumn(CustomerId attribute) {
        return attribute == null ? null : attribute.customerId();
    }

    @Override
    public CustomerId convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : new CustomerId(dbData);
    }
}
