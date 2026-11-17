package ModelBasedDesign_HybridAircraftMBDModelExample {
    doc /*
    Example showing a model-based design components of a hybrid-electric aircraft.

    Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
    */
    private import ModelBasedDesign::*;
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;
    private import USCustomaryUnits::*;
    private import SimulationAnalysis_MBD_FunctionAllocationExample::ArchitectureInstances::*;

    package Definitions {

        // reusable simulation component definitions (template library)
        #mbd_component part def MissionPerformanceSimulationTemplate {
            // meta data
            @ExecutableModelMetadata {
                :>> authoringTool = "Simcenter Amesim v2310";
            }

            // ports
            port altitude: SignalOutputPort;
            port is_climb: SignalOutputPort;
            port is_cruise: SignalOutputPort;
            port is_descent: SignalOutputPort;
            port phase_number: SignalOutputPort;

            // behavior
            #mbd_behavior perform action behavior { // or: perform action behavior: MBDBehavior :> behaviors {
                // native ports
                out altitude: ScalarValues::Real = MissionPerformanceSimulationTemplate::altitude.value;
                out is_climb: ScalarValues::Real = MissionPerformanceSimulationTemplate::is_climb.value;
                out is_cruise: ScalarValues::Real = MissionPerformanceSimulationTemplate::is_cruise.value;
                out is_descent: ScalarValues::Real = MissionPerformanceSimulationTemplate::is_descent.value;
                out phase_number: ScalarValues::Real = MissionPerformanceSimulationTemplate::phase_number.value;

                // parameters
                #mbd_parameter attribute filename: ScalarValues::String default flightMissionDataTable.uri {
                    doc /*filename for mission XYs table: X for time [s], Y1 for phase number [null], Y2 for altitude [m]*/
                } // or: attribute filename: ScalarValues::String :> parameters default "x/y/z.extension";

                // variables
                #mbd_variable attribute phaseNumber: ScalarValues::Real {doc /*phase number*/} // or: attribute phaseNumber: ScalarValues::Real :> variables;
                #mbd_variable attribute range: ISQ::LengthValue {doc /*range*/}
                #mbd_variable attribute alt: ISQ::LengthValue {doc /*altitude*/}
                #mbd_variable attribute ROC: ISQ::SpeedValue {doc /*rate of climb (ROC)*/}
                #mbd_variable attribute FPA: ISQ::AngularMeasureValue {doc /*flight path angle (FPA)*/}
                #mbd_variable attribute mach: ScalarValues::Real {doc /*Mach number (Ma)*/}
                #mbd_variable attribute TAS: ISQ::SpeedValue {doc /*true airspeed (TAS)*/}
                #mbd_variable attribute EAS: ISQ::SpeedValue {doc /*equivalent airspeed (EAS)*/}
                #mbd_variable attribute CAS: ISQ::SpeedValue {doc /*calibrated airspeed (CAS)*/}
                #mbd_variable attribute GSPD: ISQ::SpeedValue {doc /*ground speed (GSPD)*/}
                #mbd_variable attribute q: ISQ::PressureValue {doc /*dynamic pressure (q)*/}
            }

            // assets
            #mbd_asset item flightMissionDataTable {
                doc /*filename for mission XYs table: X for time [s], Y1 for phase number [null], Y2 for altitude [m]*/
                :>> type = MBDAssetType::SimulationData;
                :>> uri = "d:/temp/some_concrete_table_data.csv";
            }
        }

        #mbd_component part def ControlsHybridSimulationTemplate {
            // meta data
            @ExecutableModelMetadata {
                :>> authoringTool = "Simcenter Amesim v2310";
            }

            // ports
            port altitude: SignalInputPort;
            port is_climb: SignalInputPort;
            port is_cruise: SignalInputPort;
            port is_descent: SignalInputPort;
            port phase_number: SignalInputPort;
            port battery_SOC: SignalInputPort;
            port torque: SignalInputPort;
            port alt: SignalInputPort;
            port NP: SignalInputPort;
            port AoA: SignalOutputPort;
            port GTE_clutch: SignalOutputPort;
            port EM_clutch: SignalOutputPort;
            port pitch: SignalOutputPort;
            port flow_rate: SignalOutputPort;
            port EM_control: SignalOutputPort;

            // behavior
            #mbd_behavior perform action behavior {
                // native ports
                in altitude: ScalarValues::Real = ControlsHybridSimulationTemplate::altitude.value;
                in is_climb: ScalarValues::Real = ControlsHybridSimulationTemplate::is_climb.value;
                in is_cruise: ScalarValues::Real = ControlsHybridSimulationTemplate::is_cruise.value;
                in is_descent: ScalarValues::Real = ControlsHybridSimulationTemplate::is_descent.value;
                in phase_number: ScalarValues::Real = ControlsHybridSimulationTemplate::phase_number.value;
                in battery_SOC: ScalarValues::Real = ControlsHybridSimulationTemplate::battery_SOC.value;
                in torque: ScalarValues::Real = ControlsHybridSimulationTemplate::torque.value;
                in alt: ScalarValues::Real = ControlsHybridSimulationTemplate::alt.value;
                in NP: ScalarValues::Real = ControlsHybridSimulationTemplate::NP.value;
                out AoA: ScalarValues::Real = ControlsHybridSimulationTemplate::AoA.value;
                out GTE_clutch: ScalarValues::Real = ControlsHybridSimulationTemplate::GTE_clutch.value;
                out EM_clutch: ScalarValues::Real = ControlsHybridSimulationTemplate::EM_clutch.value;
                out pitch: ScalarValues::Real = ControlsHybridSimulationTemplate::pitch.value;
                out flow_rate: ScalarValues::Real = ControlsHybridSimulationTemplate::flow_rate.value;
                out EM_control: ScalarValues::Real = ControlsHybridSimulationTemplate::EM_control.value;

                // parameters
                #mbd_parameter attribute EM_DP_N_CLB: ISQ::AngularVelocityValue default prop_Np_CLB*GR_EM ['rev/min'] {doc /*rotor relative speed at Design Point*/}
                #mbd_parameter attribute EM_DP_N_CRS: ISQ::AngularVelocityValue default prop_Np_CRS*GR_EM ['rev/min'] {doc /*rotor relative speed at Design Point*/}
                #mbd_parameter attribute EM_DP_N_DSC: ISQ::AngularVelocityValue default prop_Np_DSC*GR_EM ['rev/min'] {doc /*rotor relative speed at Design Point*/}
                #mbd_parameter attribute EM_DP_Tq_CLB: ISQ::TorqueValue default GTE_MCP_DP_kW*1000*Hybrid_level/(EM_DP_N_CLB*2*TrigFunctions::pi/60) ['N⋅m'] {doc /*motor torque at Design Point*/}
                #mbd_parameter attribute EM_DP_Tq_CRS: ISQ::TorqueValue default GTE_MCP_DP_kW*1000*Hybrid_level/(EM_DP_N_CRS*2*TrigFunctions::pi/60) ['N⋅m'] {doc /*motor torque at Design Point*/}
                #mbd_parameter attribute EM_DP_Tq_DSC: ISQ::TorqueValue default GTE_MCP_DP_kW*1000*Hybrid_level/(EM_DP_N_DSC*2*TrigFunctions::pi/60) ['N⋅m'] {doc /*motor torque at Design Point*/}
                #mbd_parameter attribute GTE_MCP_DP_kW: ISQ::PowerValue default 1688*0.7 [kW] {doc /*Gas Turbine Engine, Design Point (cruise)*/}
                #mbd_parameter attribute Torq_target_CRS: ScalarValues::Real default prop_PCT_TM_CRS {doc /*Torque target CRS (%Tq_max)*/}
                #mbd_parameter attribute Torq_target_CLB: ScalarValues::Real default prop_PCT_TM_CLB {doc /*Torque target CLB (%Tq_max)*/}
                #mbd_parameter attribute Torq_target_DSC: ScalarValues::Real default prop_PCT_TM_DSC {doc /*Torque target DSC (%Tq_max)*/}
                #mbd_parameter attribute Hybrid_level: ScalarValues::Real default 0.4 {doc /*Degree of hybridization*/}
                #mbd_parameter attribute GR_EM: ScalarValues::Real default 2.042682927 {doc /*Gear ratio electric motor*/}
                #mbd_parameter attribute prop_Np_max: ISQ::AngularVelocityValue default 1212 [rpm] {doc /*Maximum propeller shaft rotational speed*/}
                #mbd_parameter attribute prop_Np_CRS: ISQ::AngularVelocityValue default 0.82*prop_Np_max [rpm] {doc /*Propeller shaft rotational speed at cruise*/}
                #mbd_parameter attribute prop_Np_CLB: ISQ::AngularVelocityValue default 0.82*prop_Np_max [rpm] {doc /*Propeller shaft rotational speed at climb*/}
                #mbd_parameter attribute prop_Np_DSC: ISQ::AngularVelocityValue default 0.86*prop_Np_max [rpm] {doc /*Propeller shaft rotational speed at descent*/}
                #mbd_parameter attribute prop_PCT_TM_CRS: ScalarValues::Real default 72.0 {doc /*Target cruise torque [% of max torque]*/}
                #mbd_parameter attribute prop_PCT_TM_CLB: ScalarValues::Real default 83.0 {doc /*Target climb torque [% of max torque]*/}
                #mbd_parameter attribute prop_PCT_TM_DSC: ScalarValues::Real default 25.0 {doc /*Target descent torque [% of max torque]*/}
                #mbd_parameter attribute prop_Beta_min: ISQ::AngularMeasureValue default 25 [degree] {doc /*Propeller minimum pitch*/}
                #mbd_parameter attribute prop_Beta_max: ISQ::AngularMeasureValue default 45 [degree] {doc /*Propeller maximum pitch*/}
                #mbd_parameter attribute prop_T_max: ISQ::TorqueValue default 17354/100 ['N⋅m'] {doc /*Propeller maximum torque*/}

                // variables
                #mbd_variable attribute prop_target_speed: ISQ::SpeedValue {doc /*Prop shaft speed target [rpm]*/}
                #mbd_variable attribute prop_sense_speed: ISQ::SpeedValue {doc /*Prop shaft speed sensed [rpm]*/}
                #mbd_variable attribute prop_torque_target: ISQ::TorqueValue {doc /*Prop shaft torque target [Nm]*/}
                #mbd_variable attribute prop_torque_sensed: ISQ::TorqueValue {doc /*Prop shaft torque sensed [Nm]*/}
                #mbd_variable attribute AoA_1: ISQ::AngularMeasureValue {doc /*angle of attack*/}
                #mbd_variable attribute altitude_1: ISQ::LengthValue {doc /*aircraft altitude*/}
                #mbd_variable attribute target_altitude: ISQ::LengthValue {doc /*target altitude*/}
                #mbd_variable attribute EM_torque_request: ISQ::TorqueValue {doc /*EM torque request [Nm]*/}
            }
        }

        #mbd_component part def PowerPlantSimulationTemplate {
            // meta data
            @ExecutableModelMetadata {
                :>> authoringTool = "Simcenter Amesim v2310";
            }

            // ports
            port SAP: SignalInputPort;
            port SAT: SignalInputPort;
            port SOC: SignalOutputPort;
            port thrust: MechanicalThrust3DPort;
            port EM_control: SignalInputPort;
            port fuel_rate: SignalInputPort;
            port GTE_clutch: SignalInputPort;
            port torque: SignalOutputPort;
            port NP: SignalOutputPort;
            port TAT: SignalInputPort;
            port TAP: SignalInputPort;
            port EM_clutch: SignalInputPort;
            port AoA: SignalInputPort;
            port pitch: SignalInputPort;

            // behavior
            #mbd_behavior perform action behavior {
                // native ports
                in SAP: ScalarValues::Real = PowerPlantSimulationTemplate::SAP.value;
                in SAT: ScalarValues::Real = PowerPlantSimulationTemplate::SAT.value;
                inout item thrust {
                    out thrustForceRn: VectorValues::ThreeVectorValue = PowerPlantSimulationTemplate::thrust.thrustForceRn;
                    in accelerationRgal: VectorValues::ThreeVectorValue = PowerPlantSimulationTemplate::thrust.accelerationRgal;
                    in velocityRgal: VectorValues::ThreeVectorValue = PowerPlantSimulationTemplate::thrust.velocityRgal;
                    in displacementRgal: VectorValues::ThreeVectorValue = PowerPlantSimulationTemplate::thrust.displacementRgal;
                    in bankFlightPathHeadingAngles: VectorValues::ThreeVectorValue = PowerPlantSimulationTemplate::thrust.bankFlightPathHeadingAngles;
                }
                in EM_control: ScalarValues::Real = PowerPlantSimulationTemplate::EM_control.value;
                in fuel_rate: ScalarValues::Real = PowerPlantSimulationTemplate::fuel_rate.value;
                in GTE_clutch: ScalarValues::Real = PowerPlantSimulationTemplate::GTE_clutch.value;
                in TAT: ScalarValues::Real = PowerPlantSimulationTemplate::TAT.value;
                in TAP: ScalarValues::Real = PowerPlantSimulationTemplate::TAP.value;
                in EM_clutch: ScalarValues::Real = PowerPlantSimulationTemplate::EM_clutch.value;
                in AoA: ScalarValues::Real = PowerPlantSimulationTemplate::AoA.value;
                in pitch: ScalarValues::Real = PowerPlantSimulationTemplate::pitch.value;
                out SOC: ScalarValues::Real = PowerPlantSimulationTemplate::SOC.value;
                out torque: ScalarValues::Real = PowerPlantSimulationTemplate::torque.value;
                out NP: ScalarValues::Real = PowerPlantSimulationTemplate::NP.value;

                // parameters
                #mbd_parameter attribute ns_cell: ScalarValues::Integer default 145 {doc /*number of battery cells in series*/}
                #mbd_parameter attribute np_cell: ScalarValues::Integer default 4 {doc /*number of battery cells in parallel*/}
                #mbd_parameter attribute prop_radius: ISQ::LengthValue default 3.93/2 [m] {doc /*propeller radius*/}
                #mbd_parameter attribute emotor_eff: ScalarValues::Real default 0.93 {doc /*emotor efficiency*/}
                #mbd_parameter attribute inv_eff: ScalarValues::Real default 0.98 {doc /*inverter efficiency*/}

                // variables
                #mbd_variable attribute battery_SOC: ScalarValues::Real {doc /*battery state of charge [%]*/}
                #mbd_variable attribute EM_power: ISQ::PowerValue {doc /*power delivered by electric motor [kW]*/}
                #mbd_variable attribute GTE_power: ISQ::PowerValue {doc /*power delivered by gas turbine [kW]*/}
                #mbd_variable attribute CO2_emission: ISQ::MassValue {doc /*CO2 emission [kg]*/}
                #mbd_variable attribute mission_fuel: ISQ::MassValue {doc /*fuel consumed during mission so far [kg]*/}
                #mbd_variable attribute fuel_cons: ISQ::MassFlowRateValue {doc /*actual fuel consumption [kg/s]*/}
            }
        }

        #mbd_component part def FlightDynamicsSimulationTemplate {
            // meta data
            @ExecutableModelMetadata {
                :>> authoringTool = "Simcenter Amesim v2310";
            }

            // ports
            port alt: SignalOutputPort;
            port AoA: SignalInputPort;
            port fuel_rate: SignalInputPort;
            port thrust: ~MechanicalThrust3DPort;
            port TAP: SignalOutputPort;
            port TAT: SignalOutputPort;
            port SAP: SignalOutputPort;
            port SAT: SignalOutputPort;

            // behavior
            #mbd_behavior perform action behavior {
                // native ports
                out alt: ScalarValues::Real = FlightDynamicsSimulationTemplate::alt.value;
                in AoA: ScalarValues::Real = FlightDynamicsSimulationTemplate::AoA.value;
                in fuel_rate: ScalarValues::Real = FlightDynamicsSimulationTemplate::fuel_rate.value;
                inout item thrust {
                    in thrustForceRn: VectorValues::ThreeVectorValue = FlightDynamicsSimulationTemplate::thrust.thrustForceRn;
                    out accelerationRgal: VectorValues::ThreeVectorValue = FlightDynamicsSimulationTemplate::thrust.accelerationRgal;
                    out velocityRgal: VectorValues::ThreeVectorValue = FlightDynamicsSimulationTemplate::thrust.velocityRgal;
                    out displacementRgal: VectorValues::ThreeVectorValue = FlightDynamicsSimulationTemplate::thrust.displacementRgal;
                    out bankFlightPathHeadingAngles: VectorValues::ThreeVectorValue = FlightDynamicsSimulationTemplate::thrust.bankFlightPathHeadingAngles;
                }
                out TAP: ScalarValues::Real = FlightDynamicsSimulationTemplate::TAP.value;
                out TAT: ScalarValues::Real = FlightDynamicsSimulationTemplate::TAT.value;
                out SAP: ScalarValues::Real = FlightDynamicsSimulationTemplate::SAP.value;
                out SAT: ScalarValues::Real = FlightDynamicsSimulationTemplate::SAT.value;

                // parameters
                #mbd_parameter attribute empty_mass: ISQ::MassValue default 11757+2630 [kg] {doc /*mass of empty aircraft (including battery mass)*/}
                #mbd_parameter attribute payload_mass: ISQ::MassValue default 5300 [kg] {doc /*payload mass*/}
                #mbd_parameter attribute ac_Vgnd_init_ETO_kt: ISQ::SpeedValue default 166 [knot] {doc /*aircraft ground speed at the end of takeoff*/}
                #mbd_parameter attribute ac_z_init_ETO_kt: ISQ::LengthValue default 1500 [ft] {doc /*aircraft altitude at the end of takeoff*/}
                #mbd_parameter attribute engines: ScalarValues::Real default 2 {doc /*Number of engines in the hybrid powertrain (force/torque multiplier)*/}
                #mbd_parameter attribute MTOW: ISQ::MassValue default 22000 [kg] {doc /*max takeoff weight*/}
                #mbd_parameter attribute S_ref: ISQ::AreaValue default 54.5 ['m²'] {doc /*wing reference area*/}
                #mbd_parameter attribute fuel_mass: ISQ::MassValue default 1.728e+03 [kg] {doc /*initial fuel mass*/}

                // variables
                #mbd_variable attribute mission_fuel: ISQ::MassValue {doc /*amount of fuel consumed over mission so far [kg]*/}
                #mbd_variable attribute fuel_cons: ISQ::MassFlowRateValue {doc /*actual fuel consumption [kg/s]*/}
                #mbd_variable attribute TAS: ISQ::SpeedValue {doc /*true air speed [m/s]*/}
                #mbd_variable attribute mach: ScalarValues::Real {doc /*Mach number*/}
                #mbd_variable attribute CD: ScalarValues::Real {doc /*drag coefficient*/}
                #mbd_variable attribute CL: ScalarValues::Real {doc /*lift coefficient*/}
                #mbd_variable attribute lift: ISQ::ForceValue {doc /*lift force [N]*/}
                #mbd_variable attribute drag: ISQ::ForceValue {doc /*drag force [N]*/}
                #mbd_variable attribute LDratio: ScalarValues::Real {doc /*lift-to-drag ratio*/}
                #mbd_variable attribute fuel_mass_1: ISQ::MassValue {doc /*mass of remaining fuel [kg]*/}
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
        port def ElectricalPort {
            in current: ISQ::ElectricCurrentValue;
            out voltage: ISQ::ElectricPotentialDifferenceValue;
        }
        port def LinearMechanicalPort {
            in force: ISQ::ForceValue;
            out velocity: ISQ::SpeedValue;
            out displacement: ISQ::LengthValue;
            out acceleration: ISQ::AccelerationValue;
        }
        port def RotaryMechanicalPort {
            in torque: ISQ::TorqueValue;
            out angularVelocity: ISQ::AngularVelocityValue;
            // out angularDisplacement: ISQ::AngularMeasureValue;
            // out angularAcceleration: ISQ::AngularAccelerationValue;
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
        ref :>> systemArchitecture = (turbopropArchitecture, batteryElectricArchitecture);
        // meta data
        @ExecutableModelMetadata {
            :>> authoringTool = "Simcenter System Architect v2404";
            :>> externalModelLink = ".../Demo/Hybrid_Aircraft.sarproj";
        }

        #mbd_component part flightMission: Definitions::MissionPerformanceSimulationTemplate {
            ref :>> systemArchitectureComponent = turbopropArchitecture;

            :>> behavior {
                :>> filename = "d:/temp/some_concrete_table_data.csv";
            }
        }

        #mbd_component part controls: Definitions::ControlsHybridSimulationTemplate {
            ref :>> systemArchitectureComponent = turbopropArchitecture.turboprop;

            :>> behavior {
                :>> Hybrid_level = hyb_level;
            }
        }

        #mbd_component part powerPlant: Definitions::PowerPlantSimulationTemplate {
            ref :>> systemArchitectureComponent = turbopropArchitecture.turboprop;

            :>> behavior {
                :>> ns_cell = ns_battery_cell;
                :>> np_cell = np_battery_cell;
                :>> prop_radius = propeller_radius;
                :>> emotor_eff = emotor_efficiency;
                :>> inv_eff = inv_efficiency;
            }
        }

        #mbd_component part flightDynamics: Definitions::FlightDynamicsSimulationTemplate {
            ref :>> systemArchitectureComponent = turbopropArchitecture;

            :>> behavior {
                :>> empty_mass = mass_empty;
                :>> payload_mass = mass_payload;
                :>> MTOW = MTOM;
                :>> S_ref = wing_area;
                :>> fuel_mass = mass_fuel;
            }
        }

        // interface connections
        interface flightMission.altitude to controls.altitude;
        interface flightMission.is_climb to controls.is_climb;
        interface flightMission.is_cruise to controls.is_cruise;
        interface flightMission.is_descent to controls.is_descent;
        interface flightMission.phase_number to controls.phase_number;

        interface controls.pitch to powerPlant.pitch;
        interface controls.EM_control to powerPlant.EM_control;
        interface controls.EM_clutch to powerPlant.EM_clutch;
        interface controls.GTE_clutch to powerPlant.GTE_clutch;
        interface controls.flow_rate to powerPlant.fuel_rate;
        interface controls.flow_rate to flightDynamics.fuel_rate;
        interface controls.AoA to powerPlant.AoA;
        interface controls.AoA to flightDynamics.AoA;

        interface powerPlant.SOC to controls.battery_SOC;
        interface powerPlant.NP to controls.NP;
        interface powerPlant.torque to controls.torque;
        interface powerPlant.thrust to flightDynamics.thrust;

        interface flightDynamics.SAP to powerPlant.SAP;
        interface flightDynamics.SAT to powerPlant.SAT;
        interface flightDynamics.TAP to powerPlant.TAP;
        interface flightDynamics.TAT to powerPlant.TAT;
        interface flightDynamics.alt to controls.alt;

        // exposed parameters
        #mbd_exposed_parameter attribute mass_empty: ISQ::MassValue default 12800 [kg] {doc /*empty mass (excluding battery mass) [kg]*/}
        #mbd_exposed_parameter attribute mass_payload: ISQ::MassValue default 5300 [kg] {doc /*payload mass [kg]*/}
        #mbd_exposed_parameter attribute mass_fuel: ISQ::MassValue default 1800 [kg] {doc /*mission fuel mass [kg]*/}
        #mbd_exposed_parameter attribute mass_battery_cell: ISQ::MassValue default 4.53 [kg] {doc /*mass of a battery cell [kg]*/}
        #mbd_exposed_parameter attribute ns_battery_cell: ScalarValues::Integer default 145 {doc /*number of battery cells in series*/}
        #mbd_exposed_parameter attribute np_battery_cell: ScalarValues::Integer default 4 {doc /*number of battery cells in parallel*/}
        #mbd_exposed_parameter attribute hyb_level: ScalarValues::Real default 0.4 {doc /*level of hybridization*/}
        #mbd_exposed_parameter attribute MTOM: ISQ::MassValue default 23500 [kg] {doc /*maximal take-off weight [kg]*/}
        #mbd_exposed_parameter attribute wing_area: ISQ::AreaValue default 54.5 ['m²'] {doc /*wing area [m**2]*/}
        #mbd_exposed_parameter attribute propeller_radius: ISQ::LengthValue default 3.93/2 [m] {doc /*propeller radius [m]*/}
        #mbd_exposed_parameter attribute emotor_efficiency: ScalarValues::Real default 0.93 {doc /*emotor efficiency*/}
        #mbd_exposed_parameter attribute inv_efficiency: ScalarValues::Real default 0.98 {doc /*inverter efficiency*/}

        // exposed variables
        #mbd_exposed_variable attribute time_battery_empty: ISQ::TimeValue default := 0.0 [s] {doc /*Time at which battery pack is empty*/}
        #mbd_exposed_variable attribute hybrid_range: ISQ::LengthValue default := 0.0 [km] {doc /*Range (in km) that airplane can fly in hybrid mode*/}
        #mbd_exposed_variable attribute time_fuel_empty: ISQ::TimeValue default := 0.0 [s] {doc /*Time at which all fuel is consumed*/}
        #mbd_exposed_variable attribute total_range: ISQ::LengthValue default := 0.0 [km] {doc /*Total range (in km) that airplane can fly*/}
        #mbd_exposed_variable attribute take_off_weight: ISQ::MassValue default mass_empty+mass_fuel+mass_payload+mass_battery_cell*ns_battery_cell*np_battery_cell [kg] {doc /*Take-off weight*/}
        #mbd_exposed_variable attribute SOC_battery: ScalarValues::Real default powerPlant.behavior.battery_SOC {doc /*battery state of charge*/}
        #mbd_exposed_variable attribute EM_power: ISQ::PowerValue default powerPlant.behavior.EM_power {doc /*power delivered by electric motor*/}
        #mbd_exposed_variable attribute GTE_power: ISQ::PowerValue default powerPlant.behavior.GTE_power {doc /*power delivered by gas turbine*/}
        #mbd_exposed_variable attribute fuel_cons: ISQ::MassFlowRateValue default powerPlant.behavior.fuel_cons {doc /*actual fuel consumption*/}
        #mbd_exposed_variable attribute CO2_emssion: ISQ::MassValue default powerPlant.behavior.CO2_emission {doc /*CO2 emission*/}
        #mbd_exposed_variable attribute mission_fuel: ISQ::MassValue default powerPlant.behavior.mission_fuel {doc /*fuel consumed over mission*/}
        #mbd_exposed_variable attribute altitude: ISQ::LengthValue default flightDynamics.behavior.alt_1 {doc /*aircraft altitude*/}
        #mbd_exposed_variable attribute ground_speed: ISQ::LengthValue default flightDynamics.behavior.gspd {doc /*ground speed*/}
        #mbd_exposed_variable attribute phase_number: ScalarValues::Real default flightMission.behavior.phaseNumber {doc /*mission phase number*/}

        // constraints
        assert constraint range_hybrid_mode { hybrid_range > 700.0 [km] }
        // satisfy aircraftRequirements.hybridRangeRequirement by range_hybrid_mode;

        assert constraint range_total { total_range > 900.0 [km] }
        // satisfy aircraftRequirements.toalRangeRequirement by range_total;

        assert constraint max_takeoff_weight { take_off_weight < 23500.00 [kg] }
    }
}