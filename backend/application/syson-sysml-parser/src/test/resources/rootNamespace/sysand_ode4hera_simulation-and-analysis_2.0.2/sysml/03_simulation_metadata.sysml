package SimulationMetadataExample{

    private import ModelBasedDesign::*;
    // private import RequirementsManagement::PriorityKind;

    part projectTeamMemberA : Executor {
        // :>> risk = PriorityKind::low;  // Invalid
    }

    package SimulationDefinitions{

        // Simulation Component Definitions
        
        #mbd_component part def PowerSupply {
            doc /*Power supply for the system*/
            @ExecutableModelMetadata{
                :>> authoringTool = "OpenModelica";
                :>> externalModelLink = "Flight_Control_System.BLDC.PowerSupply";
                :>> domain = "Electrical";
                :>> executor = projectTeamMemberA;
            }
        }
    }

    item exeConfig1 : ExecutionConfiguration {
        :>> startTime = 0.0;
        :>> stopTime = 1.0;
        :>> numberOfIntervals = 1000;
        :>> tolerance = 1e-6;
        :>> method = "dassl";
        :>> outputFormat = "csv";
        :>> verificationLogic = ExtractionLogic::Max;
    }

    analysis simulation_run1 :SimulationRun {
        subject SimulationFCSArch1;
        in configuration = exeConfig1;
    }

}