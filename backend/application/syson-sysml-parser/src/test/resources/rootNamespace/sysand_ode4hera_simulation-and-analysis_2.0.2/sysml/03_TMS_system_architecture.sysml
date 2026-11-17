package TMS_SystemArchitecture_Example {
    doc
    /*
    * 
    */

    private import ArchitectureDesign::*;
   
    part def <TMS> ThermalManagementSystem {

        attribute maxRejectedHeat : ISQ::HeatFlowRateValue;
        attribute maxPowerConsumption : ISQ::PowerValue default pump.maxPowerConsumption;
        attribute mass : ISQ::MassValue default pump.mass + heatExchanger.mass;
        attribute volume : ISQ::VolumeValue;
        attribute powerUpTime: ISQ::TimeValue;
        attribute powerUpRejectedHeat : ISQ::HeatFlowRateValue;

        part pump : CoolingCircuitPump;
        part heatExchanger : HeatExchanger;
        part reservoir: Reservoir;
        part coolingLiquid: CoolingLiquid;
        part controller: Controller;

    }

    action rejectHeat {
        doc /*Reject heat*/
    }
    action increaseCircuitPressure {
        doc /*Recover pressure drop after heat exchange in order to maintain circuit flow*/
    }

    // System Architecture Components
    part def CoolingCircuitPump {
        attribute <Q> flowRate : ISQ::VolumeFlowRateValue {
                doc /*The volume of coolant to be circulated per unit of time*/
        }
        attribute <H> headPressure : ISQ::PressureValue;
        attribute mass : ISQ::MassValue;
        attribute maxPowerConsumption : ISQ::PowerValue;
    }
    part def CoolingCentrifugalPump :> CoolingCircuitPump {
        attribute rotationalSpeed;
    }
    part def CoolingGearPump :> CoolingCircuitPump {
        attribute gearRadius;
    }

    part def <HX> HeatExchanger {
        attribute flowRateHot;
        attribute flowRateCold;
        attribute exchangeArea;
        attribute heatTransferRate;
        attribute heatTransferCoefficient;
        attribute pressureDrop;
        attribute mass;
        attribute volume;
    }
    part def Reservoir {
        attribute capacity;
        attribute mass;
    }
    part def CoolingLiquid {
        attribute density;
        attribute viscosity;
        attribute heatCoefficient;
    }
    part def Controller;
    part def <EPDS> ElectricPowerDistributionSystem;

    // System Architecture Design Space
    abstract #architectureDesignSpace part <tms_ads> thermalManagementSystemADS {
        doc /* Thermal management system design space */
        perform rejectHeat;

        part <tms> thermalManagementSystem : ThermalManagementSystem {

            #evalOut attribute :>> mass = pump.mass + heatExchanger.mass { // this is redundant with part def here I only wna to specify role in design space
                redefines role = OutputRole::MinimizationObjective;
            }
            
            #evalOut attribute :>> maxPowerConsumption : ISQ::PowerValue = pump.maxPowerConsumption { // same redundancy as above
                redefines role = OutputRole::Constraint;
                assert constraint { that <= TMS_SystemRequirements_Example::'R-002'::energyConsumptionLimit }
            }

            #evalOut attribute :>> maxRejectedHeat : ISQ::HeatFlowRateValue {
                redefines role = OutputRole::Constraint;
                assert constraint { that >= TMS_SystemRequirements_Example::'R-001'::heatLoadLimit} 
            }
            part centrifugalPump : CoolingCentrifugalPump {
                perform increaseCircuitPressure;
                #contX attribute :>> rotationalSpeed;
                #contX attribute :>> Q {
                    :>> upperBound : ISQ::VolumeFlowRateValue = 3e3[SI::'m³⋅s⁻¹']; // define L/s
                    :>> lowerBound : ISQ::VolumeFlowRateValue = 850[SI::'m³⋅s⁻¹'];
                }
                #contX attribute :>> H {
                    :>> upperBound : ISQ::PressureValue = 5000[SI::Pa];
                    :>> lowerBound : ISQ::PressureValue = 980[SI::Pa];
                }
            }
            part gearPump : CoolingGearPump {
                perform increaseCircuitPressure;
                #contX attribute :>> gearRadius;
                #contX attribute :>> Q {
                    :>> upperBound : ISQ::VolumeFlowRateValue = 3e3[SI::'m³⋅s⁻¹']; // define L/s
                    :>> lowerBound : ISQ::VolumeFlowRateValue = 850[SI::'m³⋅s⁻¹'];
                }
                #contX attribute :>> H {
                    :>> upperBound : ISQ::PressureValue = 5000[SI::Pa];
                    :>> lowerBound : ISQ::PressureValue = 980[SI::Pa];
                }
            }
            part :>> heatExchanger : HeatExchanger {
                #contX attribute :>> flowRateHot;
                #contX attribute :>> flowRateCold;
                #contX attribute :>> exchangeArea;
                #evalOut attribute :>> volume {
                    redefines role = OutputRole::Constraint;
                    assert constraint { that <= TMS_SystemRequirements_Example::'R-004'::maxVolume}
                }
            }
            part :>> reservoir : Reservoir {
                #contX attribute :>> capacity;
            }
            part :>> controller : Controller;
        }

        :>> formalDesignProblem {
                :>> designVariables[9] = (
                    tms.centrifugalPump.rotationalSpeed,
                    tms.centrifugalPump.Q,
                    tms.centrifugalPump.H,
                    tms.gearPump.Q,
                    tms.gearPump.H,
                    tms.gearPump.gearRadius,
                    tms.heatExchanger.flowRateHot,
                    tms.heatExchanger.flowRateCold,
                    tms.heatExchanger.exchangeArea
                );
                :>> objectives[1] = tms.mass;
                :>> constraints[3] = (tms.maxPowerConsumption, tms.maxRejectedHeat, tms.heatExchanger.volume);
        }
    }

    #architecture part <tms_gear> gearPumpThermalManagementSystem subsets tms_ads{
        part :>> thermalManagementSystem {
            part redefines gearPump {
                :>> H = 2000[SI::Pa];
            }
            part redefines centrifugalPump[0];
            part redefines heatExchanger;
            part redefines reservoir;
            part redefines controller;
        }

    }

}