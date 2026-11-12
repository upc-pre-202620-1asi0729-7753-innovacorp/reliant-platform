package com.innovacorp.reliant.platform.equipment.interfaces.acl;

/**
 * Snapshot of one recipe parameter exposed through the ACL. Primitives only.
 */
public record RecipeBandSnapshot(String parameter, double setpoint, double lowerShutdown, double lowerWarning, double nominalMin,
                                 double nominalMax, double upperWarning, double upperShutdown, String unitSymbol, String unitCategory) {
}
