package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

public record ParameterBands(
        double lowerShutdown,
        double lowerWarning,
        double nominalMin,
        double nominalMax,
        double upperWarning,
        double upperShutdown
) {
    public ParameterBands {
        if (!(lowerShutdown <= lowerWarning
                && lowerWarning <= nominalMin
                && nominalMin <= nominalMax
                && nominalMax <= upperWarning
                && upperWarning <= upperShutdown)) {
            throw new IllegalArgumentException("Parameter bands must be in ascending order");
        }
    }

    public boolean contains(double setpoint) {
        return setpoint >= nominalMin && setpoint <= nominalMax;
    }

    public Band classify(double value) {
        if (value < lowerShutdown || value > upperShutdown) {
            return Band.SHUTDOWN;
        }
        if (value < lowerWarning || value > upperWarning) {
            return Band.WARNING;
        }
        if (value < nominalMin || value > nominalMax) {
            return Band.OUT_OF_NOMINAL;
        }
        return Band.NOMINAL;
    }
}
