package com.innovacorp.reliant.platform.reporting.interfaces.rest.transform;

import com.innovacorp.reliant.platform.reporting.domain.model.commands.RegisterGeneratedReportCommand;
import com.innovacorp.reliant.platform.reporting.interfaces.rest.resources.RegisterGeneratedReportResource;

public class RegisterGeneratedReportCommandFromResourceAssembler {
    /** The organization and the user are filled by the command service from the authenticated user. */
    public static RegisterGeneratedReportCommand toCommandFromResource(RegisterGeneratedReportResource r) {
        return new RegisterGeneratedReportCommand(r.reportTemplateId(), null, null, r.subjectType(), r.subjectId(), r.fileName(), r.periodFrom(), r.periodTo());
    }
}
