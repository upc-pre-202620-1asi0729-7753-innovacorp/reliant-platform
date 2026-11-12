package com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects;

/**
 * Copy of the limits for one recipe parameter when a spray session starts.
 */
public record RecipeBand(
        String parameter,
        double setpoint,
        double lowerShutdown,
        double lowerWarning,
        double nominalMin,
        double nominalMax,
        double upperWarning,
        double upperShutdown,
        String unitSymbol,
        String unitCategory
) {
    public RecipeBand {
        if (parameter == null || parameter.isBlank()) {
            throw new IllegalArgumentException("Recipe band parameter must not be blank");
        }
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