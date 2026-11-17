package SPDMExample_HybridAircraftMBDModel {
    doc /* Models used by the SPDM example.
    
    Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
    */
    private import ModelBasedDesign::*;
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;
    private import USCustomaryUnits::*;

    // Requirement definition with subject
    part def PartWithMass {
        attribute mass : MassValue;
    }

    requirement def MassLimitRequirement specializes Requirements::PhysicalRequirementCheck {
        doc /* Requirement type that limits the mass of some part. */

        subject massPart : PartWithMass;
        attribute massLimit : MassValue;

        // Assumption so that the requirement only applies to parts that have a positive mass
        assume constraint { massPart.mass >= 0[kg] }

        require constraint { massPart.mass <= massLimit }
    }

    // Requirement usage, including shorthand notation
    requirement <'RQ-001'> massLimitRequirement : MassLimitRequirement {
        attribute redefines massLimit = 20000[kg];
    }

    package Definitions {

        // reusable simulation component definitions (template library)
        #mbd_component part def MissionPerformanceSimulationTemplate {
            // meta data
            @ExecutableModelMetadata {
                :>> authoringTool = "Simcenter Amesim v2310";
            }

            // ports
            port altitude: SignalOutputPort;


            // behavior
            #mbd_behavior perform action behavior { // or: perform action behavior: MBDBehavior :> behaviors {
                // native ports
                out altitude: ScalarValues::Real = MissionPerformanceSimulationTemplate::altitude.value;

                // parameters
                #mbd_parameter attribute filename: ScalarValues::String default flightMissionDataTable.uri {
                    doc /*filename for mission XYs table: X for time [s], Y1 for phase number [null], Y2 for altitude [m]*/
                } // or: attribute filename: ScalarValues::String :> parameters default "x/y/z.extension";

                // variables
                #mbd_variable attribute range: ISQ::LengthValue {doc /*range*/}
                #mbd_variable attribute alt: ISQ::LengthValue {doc /*altitude*/}
                #mbd_variable attribute FPA: ISQ::AngularMeasureValue {doc /*flight path angle (FPA)*/}
                #mbd_variable attribute GSPD: ISQ::SpeedValue {doc /*ground speed (GSPD)*/}
            }

            // assets
            #mbd_asset item flightMissionDataTable {
                doc /*filename for mission XYs table: X for time [s], Y1 for phase number [null], Y2 for altitude [m]*/
                :>> type = MBDAssetType::SimulationData;
                :>> uri = "d:/temp/some_concrete_table_data.csv";
            }
        }

        #mbd_component part def FlightDynamicsSimulationTemplate {
            //meta data
            @ExecutableModelMetadata {
                :>> authoringTool = "Simcenter Amesim v2310";
            }

            // behavior
            #mbd_behavior perform action behavior {

                // parameters
                #mbd_parameter attribute empty_mass: ISQ::MassValue default 11757+2630 [kg] {doc /*mass of empty aircraft (including battery mass)*/}
                #mbd_parameter attribute payload_mass: ISQ::MassValue default 5300 [kg] {doc /*payload mass*/}
                #mbd_parameter attribute MTOW: ISQ::MassValue default 22000 [kg] {doc /*max takeoff weight*/}
                #mbd_parameter attribute fuel_mass: ISQ::MassValue default 1.728e+03 [kg] {doc /*initial fuel mass*/}

                // variables
                #mbd_variable attribute mission_fuel: ISQ::MassValue {doc /*amount of fuel consumed over mission so far [kg]*/}
                #mbd_variable attribute fuel_cons: ISQ::MassFlowRateValue {doc /*actual fuel consumption [kg/s]*/}
                #mbd_variable attribute range_1: ISQ::LengthValue {doc /*range [m]*/}
                #mbd_variable attribute alt_1: ISQ::LengthValue {doc /*altitude [m]*/}
                #mbd_variable attribute gspd: ISQ::SpeedValue {doc /*ground speed [m/s]*/}
            }
        }

        // port definitions
        port def SignalInputPort {
            in value: ScalarValues::Real;
        }
        port def SignalOutputPort {
            out value: ScalarValues::Real;
        }

        port def MechanicalThrust3DPort {
            out thrustForceRn: VectorValues::ThreeVectorValue {:>>elements: ISQ::ForceValue;}
            in accelerationRgal: VectorValues::ThreeVectorValue {:>>elements: ISQ::AccelerationValue;}
            in velocityRgal: VectorValues::ThreeVectorValue {:>>elements: ISQ::SpeedValue;}
            in displacementRgal: VectorValues::ThreeVectorValue {:>>elements: ISQ::LengthValue;}
            in bankFlightPathHeadingAngles: VectorValues::ThreeVectorValue {:>>elements: ISQ::AngularMeasureValue;}
        }

        // missing units and constants
        attribute <'rev/min'> 'revolutions per minute': ISQ::AngularVelocityUnit = SI::min^-1;
        alias rpm for 'rev/min';
    }

    #mbd_architecture part hybridAircraftPerformance {
        // meta data
        @ExecutableModelMetadata {
            :>> authoringTool = "Simcenter System Architect v2404";
            :>> externalModelLink = ".../Demo/Hybrid_Aircraft.sarproj";
        }

        #mbd_component part flightMission: Definitions::MissionPerformanceSimulationTemplate {
            :>> behavior {
                :>> filename = "d:/temp/some_concrete_table_data.csv";
            }
        }

        #mbd_component part flightDynamics: Definitions::FlightDynamicsSimulationTemplate {
            :>> behavior {
                :>> empty_mass = mass_empty;
                :>> payload_mass = mass_payload;
                :>> fuel_mass = mass_fuel;
            }
        }

        // exposed parameters
        #mbd_exposed_parameter attribute mass_empty: ISQ::MassValue default 12800 [kg] {doc /*empty mass (excluding battery mass) [kg]*/}
        #mbd_exposed_parameter attribute mass_payload: ISQ::MassValue default 5300 [kg] {doc /*payload mass [kg]*/}
        #mbd_exposed_parameter attribute mass_fuel: ISQ::MassValue default 1800 [kg] {doc /*mission fuel mass [kg]*/}
        #mbd_exposed_parameter attribute mass_battery_cell: ISQ::MassValue default 4.53 [kg] {doc /*mass of a battery cell [kg]*/}

        // exposed variables
        #mbd_exposed_variable attribute time_battery_empty: ISQ::TimeValue default := 0.0 [s] {doc /*Time at which battery pack is empty*/}
        #mbd_exposed_variable attribute hybrid_range: ISQ::LengthValue default := 0.0 [km] {doc /*Range (in km) that airplane can fly in hybrid mode*/}
        #mbd_exposed_variable attribute time_fuel_empty: ISQ::TimeValue default := 0.0 [s] {doc /*Time at which all fuel is consumed*/}
        #mbd_exposed_variable attribute total_range: ISQ::LengthValue default := 0.0 [km] {doc /*Total range (in km) that airplane can fly*/}
        #mbd_exposed_variable attribute altitude: ISQ::LengthValue default flightDynamics.behavior.alt_1 {doc /*aircraft altitude*/}
        #mbd_exposed_variable attribute ground_speed: ISQ::LengthValue default flightDynamics.behavior.gspd {doc /*ground speed*/}
    }
}