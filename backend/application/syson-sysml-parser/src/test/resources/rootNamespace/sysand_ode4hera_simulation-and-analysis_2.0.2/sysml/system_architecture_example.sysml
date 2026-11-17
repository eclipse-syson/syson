package SimulationAnalysis_MBD_FunctionAllocationExample {
    doc /*
    Example of definition of function allocation and induction relationships from
    the Architecture Design library, here reused for MBD examples.
    
    Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
    */
    private import ArchitectureDesign::*;

    package ActionsLibrary {
        doc /* Example of a reusable actions library */
        
        // Abstract action definitions that define the outputs of the actions

        item def ThrustForce;
        abstract action def GenerateThrust {
            doc /* Abstract action: generate thrust */
            out thrustForce: ThrustForce;
        }
        abstract part def ThrustGenerator {
            doc /* Abstract part: thrust generator */
            perform action generateThrust: GenerateThrust;
        }

        item def ElectricPower;
        abstract action def ProvideElectricPower {
            out electricPower: ElectricPower;
        }
        abstract part def ElectricPowerProvider {
            doc /* Abstract part: electric energy provider */
            perform action provideElectricPower: ProvideElectricPower;
        }

        item def Fuel;
        abstract action def ProvideFuel {
            out fuel: Fuel;
        }
        abstract part def FuelProvider {
            perform action provideFuel : ProvideFuel;
        }
    }

    package PartsLibrary {
        doc /* Example of a reusable parts library */

        // Electric motor

        action def ElectricMotorGenerateThrustInputs {
            in electricPower: ActionsLibrary::ElectricPower;
        }
        part def ElectricMotor specializes ActionsLibrary::ThrustGenerator {
            // Perform the generate thrust action, extended with specific inputs needed for the electric motor
            perform action redefines generateThrust : ElectricMotorGenerateThrustInputs;

            // Declare the need for the provide electric power function
            #needsAction : ActionsLibrary::ProvideElectricPower;
        }

        // Battery pack and fuel cell

        part def BatteryPack :> ActionsLibrary::ElectricPowerProvider {
            perform action :>> provideElectricPower;
        }

        part def FuelCell :> ActionsLibrary::ElectricPowerProvider {
            perform action :>> provideElectricPower;
        }

        // Turboprop

        action def TurbopropGenerateThrustInputs {
            in fuel: ActionsLibrary::Fuel;
        }
        part def Turboprop :> ActionsLibrary::ThrustGenerator {
            // Perform the generate thrust action, extended with specific inputs needed for the turboprop
            perform action :>> generateThrust : TurbopropGenerateThrustInputs;

            // Declare the need for the provide fuel function
            #needsAction : ActionsLibrary::ProvideFuel;
        }

        // Fuel system

        part def FuelSystem :> ActionsLibrary::FuelProvider {
            perform action :>> provideFuel;
        }
    }

    package ArchitectureDesignSpace {
        #architectureDesignSpace part designSpace {
            doc /* Propulsion system design space,
            which generate thrust by either an electric motor or a turboprop.
            If an electric motor is chosen, it can take electric power from a battery pack or a fuel cell.
            If a turboprop is chosen, it needs fuel. */

            // Top level function
            action generateThrust : ActionsLibrary::GenerateThrust :> boundaryFunctions;

            // Alternative parts for performing the top-level function
            part electricMotor[0..1] : PartsLibrary::ElectricMotor {
                perform designSpace.generateThrust :>> generateThrust;
                #needsAction references provideElectricPower;
            }

            part turboprop[0..1] : PartsLibrary::Turboprop {
                perform designSpace.generateThrust :>> generateThrust;
                #needsAction ::> provideFuel;
            }

            // Induced functions
            action provideElectricPower[0..1] : ActionsLibrary::ProvideElectricPower;
            action provideFuel[0..1] : ActionsLibrary::ProvideFuel;

            // Alternatives for providing electric power
            part batteryPack[0..1] : PartsLibrary::BatteryPack {
                perform designSpace.provideElectricPower :>> provideElectricPower;
            }

            part fuelCell[0..1] : PartsLibrary::FuelCell {
                perform designSpace.provideElectricPower :>> provideElectricPower;
            }

            // Part for providing fuel
            part fuelSystem[0..1] : PartsLibrary::FuelSystem {
                perform designSpace.provideFuel :>> provideFuel;
            }

            // List of architectural choices
            :>> architecturalChoices {
                :>> choices[2] = (generateThrustChoice, provideElectricPowerChoice);

                occurrence generateThrustChoice :> choices : FunctionFulfillmentChoice {
                    ref :>> actionToPerform[1] = generateThrust;
                    ref :>> performingAlternatives[2] = (electricMotor, turboprop);
                }

                occurrence provideElectricPowerChoice :> choices : FunctionFulfillmentChoice {
                    ref :>> actionToPerform[1] = provideElectricPower;
                    ref :>> performingAlternatives[2] = (batteryPack, fuelCell);
                }
            }

            // Formal design problem
            :>> formalDesignProblem {
                :>> designVariables[2] = (generateThrustDesVar, provideElectricPowerDesVar);

                #catX attribute generateThrustDesVar : ScalarValues::Integer {
                    :>> values = (0, 1);
                }
                allocate architecturalChoices.generateThrustChoice to generateThrustDesVar;

                #catX attribute provideElectricPowerDesVar : ScalarValues::Integer {
                    :>> values = (0, 1);
                }
                allocate architecturalChoices.provideElectricPowerChoice to provideElectricPowerDesVar;
            }
        }
    }

    package ArchitectureInstances {
        
        #architecture part turbopropArchitecture subsets ArchitectureDesignSpace::designSpace {
            doc /* Architecture instance using the turboprop for generating thrust. */

            // Select the top-level function
            action redefines generateThrust[1];

            // Select the turboprop and its induced elements
            part :>> turboprop[1] {
                perform action redefines generateThrust[1];
            }
            action :>> provideFuel[1];
            part :>> fuelSystem[1] {
                perform action redefines provideFuel[1];
            }

            // Deselect electric components
            part :>> electricMotor[0];
            action :>> provideElectricPower[0];
            part :>> batteryPack[0];
            part :>> fuelCell[0];

            // Choice and associated design variable values
            :>> architecturalChoices {
                :>> generateThrustChoice references turboprop;
                :>> provideElectricPowerChoice = null; // inactive
            }
            :>> formalDesignProblem {
                :>> generateThrustDesVar = 1;
                :>> provideElectricPowerDesVar = null; // inactive
            }
        }
        
        #architecture part batteryElectricArchitecture subsets ArchitectureDesignSpace::designSpace {
            doc /* Architecture instance using the electric motor powered by a battery pack for generating thrust. */

            // Select the top-level function
            action redefines generateThrust[1];

            // Select the electric motor and battery pack
            part :>> electricMotor[1] {
                perform action :>> generateThrust[1];
            }
            action :>> provideElectricPower[1];
            part :>> batteryPack {
                perform action :>> provideElectricPower[1];
            }

            // Deselect fuel cell
            part :>> fuelCell[0];

            // Deselect the turboprop and its induced elements
            part :>> turboprop[0];
            action :>> provideFuel[0];
            part :>> fuelSystem[0];

            // Choice and associated design variable values
            :>> architecturalChoices {
                :>> generateThrustChoice references electricMotor;
                :>> provideElectricPowerChoice ::> batteryPack;
            }
            :>> formalDesignProblem {
                :>> generateThrustDesVar = 0;
                :>> provideElectricPowerDesVar = 0;
            }
        }

    }

}
