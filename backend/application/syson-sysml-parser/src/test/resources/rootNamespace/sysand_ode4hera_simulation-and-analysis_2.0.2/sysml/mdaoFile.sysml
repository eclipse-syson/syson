package MDAOFileExample {

    private import ArchitectureDesign::*;
    private import MultidisciplinaryDesignAnalysisOptimization::*;
    
    private import ArchitectureFileExample::*;
    private import MDAORepository::*;

    
    // =====================================================
    // Example 1: single-discipline analysis on tube-wing aircraft
    // =====================================================

    #mdaoProcessArchitecture part tubeWingAnalysisStudy {
        part :>> systemUnderAnalysis references longRangeTubeWingNominal;

        part :>> mdaoProblem {
            #minObj attribute mtow references systemUnderAnalysis.longRangeTubeWingSpace.mtow;
        }

        part :>> mdaoStrategy  {
            :>> disciplines[1] = oad;

            #discipline part oad :> disciplines : OADDiscipline {
                part :>> aircraft = systemUnderAnalysis.longRangeTubeWingSpace;
            }
        }
    }

    // =====================================================
    // Example 2: MDF optimization on blended-wing-body aircraft
    // =====================================================

    #mdaoProcessArchitecture part bwbMdfStudy {
        part :>> systemUnderAnalysis references bwbNominal;

        part :>> mdaoProblem {
            #minObj attribute fuelBurn references systemUnderAnalysis.bwbSpace.fuelBurn;
            
            #contX attribute designRange references systemUnderAnalysis.bwbSpace.designRange;
        }

        part :>> mdaoStrategy {
            :>> disciplines[3] = (aero, weight, prop);


            #discipline part aero :> disciplines : AerodynamicsDiscipline {
                part :>> aircraft = systemUnderAnalysis.bwbSpace;
                part :>> wing = systemUnderAnalysis.bwbSpace.centerBodyWing;
            }

            #discipline part weight :> disciplines : WeightDiscipline {
                part :>> aircraft = systemUnderAnalysis.bwbSpace;
            }

            #discipline part prop :> disciplines : PropulsionDiscipline {
                part :>> aircraft = systemUnderAnalysis.bwbSpace;
            }
        
            :>> mdoFormulation = MDOFormulation::Mdf;
        
        }

        
    }

    // =====================================================
    // Example 3: Optimizaiton on regional turboprop aircraft
    // =====================================================

    #mdaoProcessArchitecture part turbopropDoeStudy {
        part :>> systemUnderAnalysis references regionalTurbopropNominal;

        part turbopropProblem :>> mdaoProblem {
            #minObj attribute takeoffFieldLength references systemUnderAnalysis.regionalTurbopropSpace.takeoffFieldLength;
            
            #contX attribute designRange references systemUnderAnalysis.regionalTurbopropSpace.designRange;

            #contX attribute efficiency ::> systemUnderAnalysis.regionalTurbopropSpace.propeller.efficiency {
                :>> lowerBound = 0.85; 
                :>> upperBound = 0.89;
            }

            #con attribute rangeLimit {
                assert constraint { designRange <= 2000 }
            }
        }

        part :>> mdaoStrategy {
            :>> disciplines[2] = (oad, propeller);

            #discipline part oad :> disciplines : OADDiscipline {
                part :>> aircraft = systemUnderAnalysis.regionalTurbopropSpace;
            }

            #discipline part propeller :> disciplines : PropellerDiscipline {
                part :>> aircraft = systemUnderAnalysis.regionalTurbopropSpace;
                part :>> propeller = systemUnderAnalysis.regionalTurbopropSpace.propeller;
            }
        
            :>> mdoFormulation = MDOFormulation::Mdf;

            part :>> optimizer {
                perform action OptimizationAlgorithm :>> optimizationAlgorithm {
                    :>> method:OptimizationMethod = OptimizationMethod::GradientFree;
                    :>> algorithmName:OptimizationAlgorithmType = OptimizationAlgorithmType::Slsqp;
                }
            }

            part MDAGaussSeidelConverger :>> converger {
                :>> convergenceMethod:ConvergenceMethod = ConvergenceMethod::GaussSeidel;
            }
        
        }

        part :> mdaoSolution, turbopropProblem {

            :>> designVariablesSolution[2] = (designRange, efficiency);
            :>> designRange  = 1800.0 [SI::km];
            :>> efficiency  = 0.86;

            :>> objectiveFunctionSolution[1] = takeoffFieldLength;
            :>> takeoffFieldLength = 2.3 [SI::km];

            :>> constraintsSolution[1] = (rangeLimit);
            :>> rangeLimit = 1900;
        }
    }

}