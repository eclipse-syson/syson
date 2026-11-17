package Operational_StakeholdersAndConcerns {
    part operationalUser {
        doc
        /* The operationalUser is a stakeholder with operational level concerns */
    }
    part operationalUpkeeper {
        doc
        /* The operationalMaintenance is a stakeholder with operational maintenance concerns */
    }

    concern operationalQualityGain
        subsets MyFramework::MyFrameworkStakeholdersAndConcerns::frameworkOperationalValueStream {
        doc
        /* The quality gain of a payload in the operational context shall be at least
         * 30 quality units to ensure operational success.
         * Applicable standard-scoped parts shall also satisfy
         * MyStandard::qualityGainRequirementOfComplyingElement, whose reusable numeric
         * constraint is MyStandard::qualityGainStandardTypeA.
         */
        subject redefines operationalContext subsets Operational_Configuration::operationalContext_bb;
        stakeholder user redefines frameworkUser subsets operationalUser;

        attribute operationalQualityGain : ScalarValues::Real := 30.0;

        require constraint {
            operationalContext.qualityContribution >= operationalQualityGain
        }
    }

    concern operationalComplianceWithMyStandard {
        doc
        /* The systems in the operational context shall comply with MyStandard to ensure institutional effectivity.*/
        comment operationalComplianceWithMyStandardModelerComment
            about operationalComplianceWithMyStandard
            locale "en_US"
        /* Stakeholders ought to ensure that applicable standards are considered. */
    }

    concern operationalMaintenanceStrain
        subsets MyFramework::MyFrameworkStakeholdersAndConcerns::frameworkOperationalMaintenanceStrain {
        doc
        /* Quality gains above 10 quality units apply a high strain to the interactingSystem 
         * thus increasing the maintenance effort and reducing the operational availability.
         * In this reference pattern, the 10.0 cap is an exogenous technology envelope
         * used to seed architecture exploration and avoid circular value derivation.
         */
        subject redefines operationalContext;
        stakeholder upkeeper redefines frameworkUpkeeper subsets operationalUpkeeper;

        attribute operationalPayloadThroughInteractingSystemGain defined by ScalarValues::Real [1] = 10.0;

        require constraint {
            operationalPayloadThroughInteractingSystemGain <= 10.0
        }
    }
}

package Operational_ViewpointsAndViews {
    viewpoint payloadLifeCycle defined by MyFramework::MyFrameworkViewpoints::ValueStream {
        doc
        /* The payload life cycle viewpoint focuses on the quality gain of the payload 
         * through its life cycle in the operational context.
         */
        frame Operational_StakeholdersAndConcerns::operationalQualityGain;
        comment payloadLifeCycleModelerComment about payloadLifeCycle locale "en_US"
        /* This frame should redefine the framework concern, but that redefinition is
         * not currently supported by SysMLv2 tooling.
         */
    }

    viewpoint maintenanceValue defined by MyFramework::MyFrameworkViewpoints::OperationalResilience {
        doc
        /* The maintenance value viewpoint focuses on the maintenance strain of the payload 
         * through its life cycle in the operational context.
         */
        frame Operational_StakeholdersAndConcerns::operationalMaintenanceStrain;
        comment maintenanceValueModelerComment about maintenanceValue locale "en_US"
        /* This frame should redefine the framework concern, but that redefinition is
         * not currently supported by SysMLv2 tooling.
         */
    }

    view payloadLifeCycleOperational defined by MyFramework::MyFrameworkViews::ProductValueStream {
        doc
        /* The payload life cycle in the operational context of the black box system 
         * configuration.
         * The filters and rendering are inherited from the framework.
         */
        viewpoint payloadLifeCycle
            redefines valueStream subsets Operational_ViewpointsAndViews::payloadLifeCycle;
        expose Operational_Configuration::operationalContext_wb;
    }

    view maintenanceValueOperational
        defined by MyFramework::MyFrameworkViews::SystemMaintenanceValueStream {
        doc
        /* The maintenance value in the operational context of the white box system 
         * configuration.
         * The filters and rendering are inherited from the framework.
         */
        viewpoint maintenanceValue
            redefines operationalResilience subsets Operational_ViewpointsAndViews::maintenanceValue;
        expose Operational_Configuration::operationalContext_wb;
    }
}
