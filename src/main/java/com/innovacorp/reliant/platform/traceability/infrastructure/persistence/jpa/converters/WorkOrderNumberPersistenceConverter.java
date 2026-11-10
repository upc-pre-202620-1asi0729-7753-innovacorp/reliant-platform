package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.converters;

import com.innovacorp.reliant.platform.traceability.domain.model.valueobjects.WorkOrderNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class WorkOrderNumberPersistenceConverter implements AttributeConverter<WorkOrderNumber, String> {

    @Override
    public String convertToDatabaseColumn(WorkOrderNumber attribute) {
        return attribute == null ? null : attribute.workOrderNumber();
    }

    @Override
    public WorkOrderNumber convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new WorkOrderNumber(dbData);
    }
}