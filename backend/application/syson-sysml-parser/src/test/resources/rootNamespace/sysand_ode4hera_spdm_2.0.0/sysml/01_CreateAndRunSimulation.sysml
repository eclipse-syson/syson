package SPDM_CreateAndRunSimulationExample {
    doc /* Demonstrates how to manage the creation and execution of a Simulation Model with the use of the SPDM library.
    There is no claim of physically correctness of the example and connections to other examples.
    
    Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
    */

    private import SimulationProcessDataManagement::*;
    private import RequirementsManagement::*;
    private import SPDMExample_HybridAircraftMBDModel::*;

    package Requests {
        analysis createSimulationModel : Request {
            :>> requestType = RequestType::CreateSimulationModel;
            subject :>> simulationObject = SimulationObjects::aircraftObject;
            objective :>> selectedRequirement = SPDMExample_HybridAircraftMBDModel::massLimitRequirement;
        }

        analysis runSimulation : Request {
            :>> requestType = RequestType::RunSimulation;
            :>> simulationStrategy = SimulationStrategies::singleRun;
            subject :>> simulationObject = SimulationObjects::aircraftObject;
            objective :>> selectedRequirement = SPDMExample_HybridAircraftMBDModel::massLimitRequirement;
            ref attribute simulatedMass :>> selectedSimulationResult = 180 [SI::kg];
            return :>> verificationStatus = VVStatus::CompletedPassed;
        }
    }

    package SimulationStrategies {
        part singleRun : SimulationStrategy {
            in :>> rawCompleteSimulationResult = SimulationModels::aircraftModel.rawCompleteSimulationResult;
            :>> toolConfiguration {
                :>> neededSimulationApplicationVersion = SimulationApplications::simulationTool1D.version;
            }
            :>> simulationApplication = SimulationApplications::simulationTool1D;
        }        
    }

    package SimulationObjects {
        aircraftObject : SimulationObject {
            :>> simulationModel = SimulationModels::aircraftModel;
            :>> spdmAttributes = SPDMExample_HybridAircraftMBDModel::massLimitRequirement.massLimit;
            allocate SPDMExample_HybridAircraftMBDModel::massLimitRequirement.massLimit to SPDMExample_HybridAircraftMBDModel::hybridAircraftPerformance.mass_empty;
        }
    }

    package SimulationModels {
        part aircraftModel : SimulationModel {
            :>> modelPath = "C:\\Models\\AircraftModel";
            :>> simulationModelType = SimulationModelType::BehaviorModel;
            out :>> rawCompleteSimulationResult = "C:\\Models\\SimulationResults\\Result1";
            :>> exposedParameters = (SPDMExample_HybridAircraftMBDModel::hybridAircraftPerformance.mass_empty, SPDMExample_HybridAircraftMBDModel::hybridAircraftPerformance.mass_empty, SPDMExample_HybridAircraftMBDModel::hybridAircraftPerformance.mass_payload, SPDMExample_HybridAircraftMBDModel::hybridAircraftPerformance.hybrid_range);
        }
    }

    package SimulationApplications {
        part simulationTool1D : SimulationApplication {
            :>> version = "12";
            :>> toolName = "1DSimulation";
            :>> uri = "C:\\Programs\\1DSimulationTool";
        }
    }
}
