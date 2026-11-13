# Changelog

All notable changes to the Reliant Platform are documented in this file. The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project adheres to [Semantic Versioning](https://semver.org/).

## [1.0.0] - 2026-11-13
### Added
- IAM bounded context: organization sign-up with administrator, JWT sign-in, roles catalog, user listing and role assignment.
- Equipment bounded context: HVOF systems with subsystems and controllers, process recipes with nominal, warning and shutdown bands and the DRAFT → ACTIVE → RETIRED publication cycle.
- Traceability bounded context: customers, recovered components and recuperation orders linked to spray sessions.
- Process Monitoring bounded context: spray sessions with recipe bands frozen at start, reading ingestion classified by band, deviation counts, passes, completion and abortion.
- Fault Diagnosis bounded context: cause-effect rules (FESA defaults seeded per organization) and fault cases opened automatically from out-of-band readings, with acknowledgement and resolution.
- Reporting bounded context: customizable report templates (quality certificate and session report seeded per organization) and the registry of generated reports.
- Shared kernel: `Result`/`ApplicationError`, localized RFC 9457 problem details (EN/ES), domain event publication, CORS, OpenAPI with bearer authentication.
- Production profile for Azure App Service and MySQL Flexible Server with the schema created on startup.
