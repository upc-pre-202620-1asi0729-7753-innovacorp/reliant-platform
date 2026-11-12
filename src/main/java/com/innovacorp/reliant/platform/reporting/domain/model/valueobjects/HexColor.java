package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;

/** A #RRGGBB color used by the template branding. */
public record HexColor(String value) {
    public HexColor {
        if (value == null || !value.trim().matches("^#[0-9A-Fa-f]{6}$")) {
            throw new IllegalArgumentException("Color must be a #RRGGBB value");
        }
        value = value.trim().toUpperCase();
    }
}
