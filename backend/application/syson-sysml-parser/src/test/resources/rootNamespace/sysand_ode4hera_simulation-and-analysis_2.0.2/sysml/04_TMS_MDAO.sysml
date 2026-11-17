package MDAO_TMS {


  private import TMS_SystemRequirements_Example::*;
  private import TMS_SystemArchitecture_Example::*;
  private import MultidisciplinaryDesignAnalysisOptimization::*;
  private import ArchitectureDesign::*;
  
  private import ScalarValues::*;
  private import ISQ::*;
  private import SI::*;

  #mdaoProcessArchitecture part TMS_MDAO{
        
      ref part redefines systemUnderAnalysis redefines gearPumpThermalManagementSystem; // Instantiate the system

      part tmsProblem :>> mdaoProblem{

            :>> designVariables[4] = (input_pump_power, channel_width, channel_height, channel_length);

            #contX attribute input_pump_power:Real default 1000{
                  redefines lowerBound = 0.0; redefines upperBound = TMS_SystemRequirements_Example::'R-002'::energyConsumptionLimit;
            }
      
            #contX attribute channel_width:Real default 0.01{
                  redefines lowerBound = 0.001; redefines upperBound = 0.1;
            }
            #contX attribute channel_height : Real default 0.002{
                  redefines lowerBound = 0.001; redefines upperBound = 0.005;
            }
            #contX attribute channel_length : Real default 0.1{
                  redefines lowerBound = 0.01; redefines upperBound = 0.5;
            } 
                 
            :>> objectives[1] = total_mass;
            #minObj attribute total_mass :> systemUnderAnalysis.tms.mass;

            :>> constraints[2] = (total_mass_constraint, systemUnderAnalysis.tms.maxRejectedHeat);

            #con attribute total_mass_constraint : Real {
                  assert constraint { that <= TMS_SystemRequirements_Example::'R-003'::maxMass }
            }
      }

       part tmsStrategy :>> mdaoStrategy {

            :>> mdoFormulation = MDOFormulation::Mdf;
            :>> scenarioType = MDOScenarioType::Mdo;

            :>> disciplines[4] = (EstimatePumpMassDiscipline, ComputePumpMassFlowDiscipline, ThermalManagementSystemDiscipline, ComputeTotalMassDiscipline);

            #discipline part EstimatePumpMassDiscipline{
                  ref part gearPump = systemUnderAnalysis::tms::gearPump;
                  ref part centrifugalPump = systemUnderAnalysis::tms::centrifugalPump;
                  in input_pump_power:Real;
                  out pump_mass:Real;
            }

           
            #discipline part ComputePumpMassFlowDiscipline{     
                  ref part gearPump = systemUnderAnalysis::tms::gearPump;
                  ref part centrifugalPump = systemUnderAnalysis::tms::centrifugalPump;
                  in input_pump_power : Real;
                  out mdot_coolant : Real;
            }

            #discipline part ThermalManagementSystemDiscipline{
                  ref part heatExchanger = systemUnderAnalysis::tms::heatExchanger;
                  ref part reservoir = systemUnderAnalysis::tms::reservoir;
                  ref part controller = systemUnderAnalysis::tms::controller;

                  in channel_width:Real;
                  in channel_height:Real;
                  in channel_length:Real;
                  in mdot_coolant:Real;

                  #mdaoParameter attribute max_power_rating: ISQ::PowerValue default 1 * 10**4 ['W'];
                  #mdaoParameter attribute q_in: ISQ::PowerValue default 500.0 ['W'];
                  #mdaoParameter attribute mass: ISQ::MassValue default 50.0 ['kg'];
                  #mdaoParameter attribute T_in: ISQ::TemperatureValue default 300.0 ['K'];
                  #mdaoParameter attribute T_surface: ISQ::TemperatureValue default 320.0 ['K'];
                  //#MDAOParameter attribute n_parallel: ScalarValues::Integer default 10;
                  

                  out q_exchanger:HeatFlowRateValue;
                  out T_out:Real;
                  out exchanger_mass:Real;
            }

            #discipline part ComputeTotalMassDiscipline{
                  ref part heatExchanger = systemUnderAnalysis::tms::heatExchanger;
                  
                  doc/*
                  * total_mass = pump_mass + exchanger_mass
                  */
                  in pump_mass : Real;
                  in exchanger_mass:Real;

                  out total_mass:Real;
            }

            bind EstimatePumpMassDiscipline.pump_mass = ComputeTotalMassDiscipline.pump_mass;
            bind ComputePumpMassFlowDiscipline.mdot_coolant = ThermalManagementSystemDiscipline.mdot_coolant;
            bind ThermalManagementSystemDiscipline.exchanger_mass = ComputeTotalMassDiscipline.exchanger_mass;

            part tmsOptimizer :>> optimizer {
                perform action tmsOptimizationAlgorithm :>> optimizationAlgorithm {
                    :>> method:OptimizationMethod = OptimizationMethod::GradientFree;
                    :>> algorithmName:OptimizationAlgorithmType = OptimizationAlgorithmType::Cobyla;
                    :>> maxIter = 1000;
                }
            }

            part mdaGaussSeidelConverger :>> converger {
                :>> convergenceMethod:ConvergenceMethod = ConvergenceMethod::GaussSeidel;
            }
      }

      part tmsSolution :> mdaoSolution, tmsProblem {

  
      }   
      

      bind tmsProblem.input_pump_power = tmsStrategy.EstimatePumpMassDiscipline.input_pump_power;
      bind tmsProblem.input_pump_power = tmsStrategy.ComputePumpMassFlowDiscipline.input_pump_power;

      bind tmsProblem.channel_width = tmsStrategy.ThermalManagementSystemDiscipline.channel_width;
      bind tmsProblem.channel_height = tmsStrategy.ThermalManagementSystemDiscipline.channel_height;
      bind tmsProblem.channel_length = tmsStrategy.ThermalManagementSystemDiscipline.channel_length;

      bind tmsStrategy.ComputeTotalMassDiscipline.total_mass = tmsProblem.total_mass_constraint;
      bind tmsStrategy.ThermalManagementSystemDiscipline.q_exchanger = systemUnderAnalysis.tms.maxRejectedHeat;
  }
}