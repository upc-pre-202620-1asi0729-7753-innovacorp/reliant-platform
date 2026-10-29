package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

@Embeddable
public record HvofSystemCode(String code) {
    private static final int MAX_LENGTH = 32;
    private static final Pattern ALLOWED = Pattern.compile("^[A-Z0-9][A-Z0-9-]*$");

    public HvofSystemCode {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("hvof.system.error.code.notBlank");
        if (code.length() > MAX_LENGTH) throw new IllegalArgumentException("hvof.system.error.code.size");
        if (!ALLOWED.matcher(code).matches()) throw new IllegalArgumentException("hvof.system.error.code.pattern");
    }
}