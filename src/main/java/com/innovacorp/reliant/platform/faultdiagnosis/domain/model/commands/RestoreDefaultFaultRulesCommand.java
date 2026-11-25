
package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.commands;

/**
 * Command to add back every FESA default rule the organization is missing, keeping the rules it already has.
 */
public record RestoreDefaultFaultRulesCommand(Long organizationId) {
}
