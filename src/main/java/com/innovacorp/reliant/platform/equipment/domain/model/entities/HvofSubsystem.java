package com.innovacorp.reliant.platform.equipment.domain.model.entities;

import com.innovacorp.reliant.platform.equipment.domain.model.valueobjects.SubsystemType;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class HvofSubsystem {
    private Long id;
    private final SubsystemType subsystemType;
    private final String name;
    private final String alias;
    private final List<String> parameters;

    public HvofSubsystem(SubsystemType subsystemType, String name, String alias, List<String> parameters) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Subsystem name must not be blank");
        if (alias == null || alias.isBlank()) throw new IllegalArgumentException("Subsystem alias must not be blank");
        var cleaned = (parameters == null ? List.<String>of() : parameters).stream().map(String::trim).distinct().toList();
        if (cleaned.stream().anyMatch(p -> !p.matches("^[a-z][a-z0-9_]{2,39}$"))) {
            throw new IllegalArgumentException("Parameter names must be snake_case, e.g. fuel_gas_flow");
        }
        this.subsystemType = Objects.requireNonNull(subsystemType, "subsystemType must not be null");
        this.name = name.trim();
        this.alias = alias.trim();
        this.parameters = cleaned;
    }

    public static HvofSubsystem reconstitute(Long id, SubsystemType subsystemType, String name, String alias, List<String> parameters) {
        var subsystem = new HvofSubsystem(subsystemType, name, alias, parameters);
        subsystem.id = id;
        return subsystem;
    }
}