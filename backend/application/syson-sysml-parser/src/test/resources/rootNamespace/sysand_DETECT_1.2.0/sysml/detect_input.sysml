package DETECT_Input {
    doc Acknowledgement
    /* This file contains content from DETECT model file for SysML v1.6 by Daniel Hettema, OUSD (R&E).
     * Original content is dedicated to the Public Domain.
     * Modifications Copyright (c) 2025 Sensmetry.
     */

    doc Description
    /* Determine DE Ecosystem Sizing according to DETECT method */

    private import DETECT_Sizing::*;

    part de_ecosystem : DETECT_Sizing::DE_Ecosystem {
        doc
        /* Instance of DE system, that is being sized */

        item :>> inputs {
            // ---------------------------------------------------------
            // DIGITAL ENGINEERING ECOSYSTEM INSTANCE (ENTER DATA HERE)
            //
            // VS Code Windows users: delete the "TBD" parts below and press CTRL + Space
            // to select enum value from dropdown bar (with blue icon)

            attribute :>> number_of_users = DETECT_enumerations::Users_e::TBD;

            attribute :>> number_of_partners = DETECT_enumerations::CollaborationPartners_e::TBD;

            attribute :>> engineering_domains = DETECT_enumerations::EngineeringDomains_e::TBD;

            attribute :>> geographic_locations = DETECT_enumerations::GeographicLocations_e::TBD;

            attribute :>> lifecycle_phases = DETECT_enumerations::LifecyclePhases_e::TBD;

            attribute :>> automation = DETECT_enumerations::Automation_e::TBD;

            attribute :>> enclaves = DETECT_enumerations::Enclaves_e::TBD;

            attribute :>> tenants = DETECT_enumerations::Tenants_e::TBD;

            attribute :>> project_deliverables = DETECT_enumerations::ProjectDeliverables_e::TBD;

            attribute :>> storage = DETECT_enumerations::Storage_e::TBD;
            // ---------------------------------------------------------
        }

        assert constraint no_TBD_values {
            inputs.number_of_users.value != 0 &
            inputs.number_of_partners.value != 0 &
            inputs.engineering_domains.value != 0 &
            inputs.geographic_locations.value != 0 &
            inputs.lifecycle_phases.value != 0 &
            inputs.automation.value != 0 &
            inputs.enclaves.value != 0 &
            inputs.tenants.value != 0 &
            inputs.project_deliverables.value != 0 &
            inputs.storage.value != 0
        }

        derived attribute :>> system_size_number =
            inputs.number_of_users.value +
            inputs.number_of_partners.value +
            inputs.engineering_domains.value +
            inputs.geographic_locations.value +
            inputs.lifecycle_phases.value +
            inputs.automation.value +
            inputs.enclaves.value +
            inputs.tenants.value +
            inputs.project_deliverables.value +
            inputs.storage.value;

        derived attribute :>> system_size =
            if system_size_number > 28
                ?    DETECT_enumerations::Ecosystem_size_e::Large
                else if system_size_number > 17
                ?    DETECT_enumerations::Ecosystem_size_e::Medium
                else DETECT_enumerations::Ecosystem_size_e::Small;
    }
}
