package FlightControlSystemExample{

    private import ScalarValues::*;
    private import ISQBase::*;
    private import ISQElectromagnetism::*;
    private import Metaobjects::*;
    private import ModelBasedDesign::*;

    package FCSimulationDefinitions{

        // Port Definitions
        port def ElectricPin {
            doc /*Electrical connector*/
            <i> current :> ISQ::electricCurrent; // Potential at the pin
            <v> voltage :> ISQ::electricPotential; // Current flowing into the pin (positive if flowing in) 
            @ExecutableModelMetadata {
                :>> authoringTool = "OpenModelica";
                :>> externalModelLink = "Modelica.Electrical.Analog.Interfaces.Pin"; // Modelica standard library path
                :>> domain = "Electrical";
            }
        }

        port def SignalInput {
            doc /*Input signal*/
            in value: ScalarValues::Real;
            @ExecutableModelMetadata { // port are not really executable, interesting here is the externalModelLink
                :>> authoringTool = "OpenModelica";
                :>> externalModelLink = "Modelica.Blocks.Interfaces.RealInput";
                :>> domain = "Signal";
            }
        }

        // Simulation Component Definitions
        
        #mbd_component part def PowerSupply {
            doc /*Power supply for the system*/
            port pin_p : ElectricPin;
            port pin_n : ElectricPin;
            @ExecutableModelMetadata{
                :>> authoringTool = "OpenModelica";
                :>> externalModelLink = "Flight_Control_System.BLDC.PowerSupply"; // Modelica custom library path
                :>> domain = "Electrical";
            }
        }

        #mbd_component part def ThreePhase_Inverter_Average {
            doc
            /* 
            Power Bridge Average Model
            */

            inout port dc_pos : ElectricPin;
            inout port dc_neg : ElectricPin;
            in port d_a : SignalInput;
            in port d_b : SignalInput;
            in port d_c : SignalInput;
            out port phase_a : ElectricPin;
            out port phase_b : ElectricPin;
            out port phase_c : ElectricPin;

            @ExecutableModelMetadata about ThreePhase_Inverter_Average {
                :>> authoringTool = "OpenModelica";
                :>> externalModelLink = "Flight_Control_System.BLDC.ThreePhase_Inverter_Average"; // Modelica custom library path
                :>> domain = "Electronics";
            }
        }

        #mbd_component part def Environment {

            #mbd_parameter attribute p_ambient : Real default 2*101325 {doc /* Ambient pressure */}
            #mbd_parameter attribute T_ambient : Real default 293.15 {doc /* Ambient temperature */}

            @ExecutableModelMetadata{
                :>>externalModelLink = "OpenHydraulics.Circuits.Environment";
            }
        }

        #mbd_component part def PilotInput {
            out port cmd : SignalInput;
        }
    }
    #mbd_architecture part FlightControlSystemArch1 {
        #mbd_component part inverter : FCSimulationDefinitions::ThreePhase_Inverter_Average;
        #mbd_component part power_supply :FCSimulationDefinitions::PowerSupply;
        // Network
        interface power_supply.pin_p to inverter.dc_pos;
        interface power_supply.pin_n to inverter.dc_neg;
    }

    #mbd_architecture part SimulationFCSArch1 {
        #mbd_architecture part simulationArchitecture = FlightControlSystemArch1;

        #mbd_component part environment : FCSimulationDefinitions::Environment;
        #mbd_component part pilotInput : FCSimulationDefinitions::PilotInput;

        interface simulationArchitecture.inverter.d_a to pilotInput.cmd;

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