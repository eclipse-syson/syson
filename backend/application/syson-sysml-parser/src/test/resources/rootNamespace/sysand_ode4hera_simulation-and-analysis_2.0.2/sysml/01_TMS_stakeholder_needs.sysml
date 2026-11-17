private import RequirementsManagement::*;
private import ArchitectureDesign::*;
private import SYSMOD::*;

package TMS_StakeholderNeedsRequirements_Example {
    doc 
    /*
    * [ISO 15288 6.4.2.1] The purpose of the Stakeholder Needs and Requirements Definition process is to 
    * define the stakeholder needs and requirements for a system that can provide the
    * capabilities needed by users and other stakeholders in a defined environment.
    * 
    * [MagicGrid] All processes associated with Problem Domain, including Stakeholder Needs,
    * Black Box (Operational Scenario: System Context, Use Cases, Measure of Effectiveness) and
    * White Box (Stakeholder Requirements: Conceptual Subsystems, Functional Analysis, MoEs for
    * subsystems). 
    */

package TMS_Stakeholders {

    part <Stake01> airline: Stakeholder {
        doc /* Specific airline company */
        attribute :>> risk = SYSMOD::LevelKind::high;
        contact :>> contact= "point.of.contact@airline.com";
        :>> categories = StakeholderCategoryKind::user;
    }
    part <Stake02> aircraft_oem: Stakeholder {
        doc /* Specific aircraft manufacturer */
    }
    part <Stake03> tms_supplier: Stakeholder {
        doc /* Specific TMS manufacturer */
    }
    part <Stake04> pilot: Stakeholder {
        doc /* Aircraft pilot */
    }

}

package TMS_StakeholderNeeds {
    private import RiskMetadata::*;
    private import TMS_Stakeholders::*;

    concern <Need01> cabin_temperature_concern: Need {
        doc /* Comfortable temperature for passengers and pilot */
        subject subj;
        stakeholder airline references Stake01;
        require constraint {
            doc /* The cabin temperature shall stay within specified range
            in each operational conditions */
        }

        // Attributes related to traceability
        :>> rationale= "Passengers comfort";
        :>> source= "Passenger comfort standard";

        // Attributes related to need/requirement authoring and verification/validation
        :>> owner= airline;
        :>> status= ReqStatus::InDevelopment;
        :>> version= "01";
        :>> createdDate= new Time::Iso8601DateTime(new Time::Iso8601DateTimeEncoding("2021-05-19T12:00:00+02:00"));
        :>> comments= "Comfortable temperature for passengers and pilot";

        // Attributes related to implementation of the need/requirement
        :>> priority= ReqPriority::MustHave;
        :>> implementationResponsible= tms_supplier;
        :>> implementationStatus= ReqImplementationStatus::UnderReview;

        // Attributes related to risk
        :>> stability= ReqStability::Stable;
        metadata ecsFailure: Risk {
            doc /*Failure in the environmental control system*/
            technicalRisk= cabin_temperature_concern.ecsFailureRiskLevel;
        }
        attribute ecsFailureRiskLevel: RiskLevel {
                :>> probability= new Level(0.4);
                :>> impact= new Level(0.3);
            }
        :>> implementationRisk = ecsFailure;
        :>> riskMitigation = "Implement redundancy";

        // Attributes related to verification/validation of the system against the need/requirement
        :>> verificationMethod = "Simulation";
        :>> verificationResponsible = aircraft_oem;
        :>> verificationStatus = VVStatus::NotStarted;
    }
    concern <Need02> obs_reliability_concern: Need {
        doc /* On-Board Systems reliability */
        subject subj;
        stakeholder pilot references Stake04;
        require constraint {
            doc /* The OBS shall work in all operating conditions */
        }
        ref obs_temperature_concern;
        :>> implementationResponsible= aircraft_oem;
        :>> verificationResponsible references aircraft_oem;
    }
    concern <Need03> obs_temperature_concern: Need {
        doc /* On-Board Systems temperature is controlled */
        subject subj;
        require constraint {
            doc /* The OBS temperature stays within the specified range 
            in all operating conditions */
        }
        stakeholder oem references tms_supplier;
        :>> implementationResponsible= tms_supplier;
        :>> verificationResponsible= aircraft_oem;
    }

}

part <tms> thermalManagementSystem :System{
    doc /*TMS part usage, this is the System of Interest (SoI)*/
}

package TMS_ContextParticipants {

    part <obs> onBoardSystems : System {
        doc /*Container for all OBS excluding TMS and ES*/
    }
    part <epds> electricPowerDistributionSystem : System {
        doc /*EPDS part usage*/
    }
    part cabin : System {
        doc /*Cabin*/
    }
    part <atm> atmosphere : System {
        doc /*Atmosphere*/
    }
    part crew {
        doc /*Airline crew*/
    }
}

package TMS_SystemContexts {
    private import TMS_DataDictionary_Example::*;
    private import TMS_ContextParticipants::*;

    use case tms_in_use:SystemContext {
        doc /*Context for TMS in use*/
        subject = tms;
        actor :> actors = crew;
        actor :> actors = obs;
        actor :> actors = epds;
        actor :> actors = atm;
        actor :> actors = cabin;

        message of AirFlow from atm to tms;
        message of AirFlow from tms to atm;

        message of CommandTemperature from crew to thermalManagementSystem;
        message of AirPressure from cabin to tms;
        message of AirTemperature from cabin to tms;
        // message of AirFlow from cabin to thermalManagementSystem;
        message of AirFlow from tms to cabin;
        
        message of Temperature from obs to tms;
        message of CoolingFlow from obs to tms;
        message of CoolingFlow from tms to obs;
        
        message of ElectricCurrent from epds to tms;
        message of ElectricCurrent from tms to epds;
    }
}

package TMS_UseCases {
    private import TMS_StakeholderNeeds::*;
    private import TMS_ContextParticipants::*;
    private import TMS_SystemContexts::*;

    use case control_obs_temperature {
        subject thermalManagementSystem;
        objective {
            doc /*Mantain on-board system temperature within specified range*/
            require obs_temperature_concern;
        }
        actor onBoardSystems;

        first start;
        
    }


}

}