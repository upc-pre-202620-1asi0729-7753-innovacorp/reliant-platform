package com.innovacorp.reliant.platform.equipment.application.commandservices;

public sealed interface HvofSystemCommandFailure permits HvofSystemCommandFailure.Duplicate {
    String messageKey();

    record Duplicate() implements HvofSystemCommandFailure {
        @Override
        public String messageKey() { return "hvof.system.error.duplicate"; }
    }
}