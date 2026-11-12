package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects.UserId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserIdPersistenceConverter implements AttributeConverter<UserId, Long> {
    @Override
    public Long convertToDatabaseColumn(UserId attribute) { return attribute == null ? null : attribute.userId(); }

    @Override
    public UserId convertToEntityAttribute(Long dbData) { return dbData == null ? null : new UserId(dbData); }
}
