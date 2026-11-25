package com.innovacorp.reliant.platform.iam.domain.model.valueobjects;


public enum OrganizationType {
    RECUPERATION_SUPPLIER, ASSET_OWNER;

    public static OrganizationType fromString(String value) {
        try {
            return OrganizationType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.organization-type.invalid");
        }
    }
}