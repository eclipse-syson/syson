package Sobieski_Tensors_UMDO_Example {
    doc /* The Sobieski's Super-Sonic Business Jet problem for UMDAO and Tensors illustration.
    From: https://gemseo.gitlab.io/dev/gemseo-umdo/latest/generated/examples/umdo/sampling/s_sobieski/
    
    Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
    */

    private import ArchitectureDesign::*;
    private import MultidisciplinaryDesignAnalysisOptimization::*;
    private import MultidisciplinaryDesignAnalysisOptimization::Uncertainty::*;
    private import ProbabilityAndSampling::*;
    private import ScalarValues::*;
    private import TensorVectorMatrix::*;

    #umdaoProcessArchitecture part SobieskiMDAO{

        part sobieskiProblem :>> umdaoProblem {

            :>> designVariables[4] = (x_shared, x_1, x_2, x_3);
        
            
            #contX attribute x_shared_0 : Real default 0.05 { redefines lowerBound = 0.01; redefines upperBound = 0.09; }

            #contX attribute x_shared_1 : Real default 45000.0 { redefines lowerBound = 30000.0; redefines upperBound = 60000.0; }

            #contX attribute x_shared_2 : Real default 1.6 { redefines lowerBound = 1.4; redefines upperBound = 1.8; }

            #contX attribute x_shared_3 : Real default 5.5 { redefines lowerBound = 2.5; redefines upperBound = 8.5; }

            #contX attribute x_shared_4 : Real default 55.0 { redefines lowerBound = 40.0; redefines upperBound = 70.0; }

            #contX attribute x_shared_5 : Real default 1000.0 { redefines lowerBound = 500.0; redefines upperBound = 1500.0; }

            #contX attribute x_1_0 : Real default 0.25 { redefines lowerBound = 0.1; redefines upperBound = 0.4; }

            #contX attribute x_1_1 : Real default 1.0 { redefines lowerBound = 0.75; redefines upperBound = 1.25; }

            #contX attribute x_2 : Real default 1.0 { redefines lowerBound = 0.75; redefines upperBound = 1.25; }

            #contX attribute x_3 : Real default 0.5 { redefines lowerBound = 0.1; redefines upperBound = 1.25; }


            // (A) Shared design vector x_shared[0..5]
            attribute x_shared : DesignVariablesVector {
                :>> tensor.shape = (6);
                :>> tensor.dimensionLabels = ("x_shared");
                :>> tensor.dimensionSemantics = ("x_shared_index");
                :>> values = (x_shared_0, x_shared_1, x_shared_2, x_shared_3, x_shared_4, x_shared_5);
            }

            // (B) Local design vectors
            attribute x_1 : DesignVariablesVector {
                :>> tensor.shape = (2);
                :>> tensor.dimensionLabels = ("x_1");
                :>> tensor.dimensionSemantics = ("x_1_index");
                :>> values = (x_1_0, x_1_1);
            }

            :>> vectors  = (x_shared, x_1);

            :>> uncertainVariables[2] = (c_4, u_x_2);

             #uncertainVariable attribute c_4 {
                doc /* Uncertain constant c_4 ~ Normal(mu=0.01375, sigma=0.01375*0.05) */
                :>> distribution :> NormalDistribution {
                    :>> mean   = 0.01375;
                    :>> stdDev = 0.01375 * 0.05;
                }
            }

            #uncertainVariable attribute u_x_2 {
                doc /* Additive uncertainty term for x_2: x_2_eff = x_2 + u_x_2, u_x_2 ~ Normal(0, 1*0.05) */
                :>> distribution :> NormalDistribution {
                    :>> mean   = 0.0;
                    :>> stdDev = 1.0 * 0.05;
                }
            }

            :>> objectives[1] = y_4;

            #maxObj attribute y_4;
           
            // Scalar components (produced by Structure discipline)
            attribute g1_1 : Real;
            attribute g1_2 : Real;
            attribute g1_3 : Real;
            attribute g1_4 : Real;
            attribute g1_5 : Real;
            attribute g1_6 : Real;
            attribute g1_7 : Real;
            attribute g3_1 : Real;
            attribute g3_2 : Real;
            attribute g3_3 : Real;

            #umdaoConstraint attribute g1: TensorVector {
                
                :>> tensor.shape = (7);
                :>> tensor.dimensionLabels = ("k");
                :>> tensor.dimensionSemantics = ("StructureConstraintIndex");
                :>> values = (g1_1, g1_2, g1_3, g1_4, g1_5, g1_6, g1_7);
                
                :>> uqConstraintType = UQConstraintType::Margin; 
                :>> statisticType = StatisticType::StandardDeviation; 
                :>> marginFactor = 2.0; 
                
                assert constraint { values <= 0.0 }
            }

            #umdaoConstraint attribute g2 { 
                :>> uqConstraintType = UQConstraintType::Margin; 
                :>> statisticType = StatisticType::StandardDeviation; 
                :>> marginFactor = 2.0; 
                assert constraint { that <= 0.0 } 
            }


            #umdaoConstraint attribute g3 : TensorVector {             
                :>> tensor.shape = (3);
                :>> tensor.dimensionLabels = ("k");
                :>> tensor.dimensionSemantics = ("PropulsionConstraintIndex");
                :>> values = (g3_1, g3_2, g3_3);     

                :>> uqConstraintType = UQConstraintType::Margin; 
                :>> statisticType = StatisticType::StandardDeviation; 
                :>> marginFactor = 2.0; 

                assert constraint { values <= 0.0 }
            }

        }

        part sobieskiStrategy :>> umdaoStrategy {
            doc
            /*
             * MDF-style coupling (as in GEMSEO examples):
             * - Strong coupling among Structure(1), Aerodynamics(2), Propulsion(3)
             * - Mission(4) weakly coupled: consumes L/D, W_T, W_F, SFC and outputs range
             *
             * Variable naming follows GEMSEO benchmark conventions:
             * - coupling y_ij: output of discipline i, input of discipline j
             * - objective range is y_4[0] (Mission output)
             */

            :>> mdoFormulation = MDOFormulation::Mdf;
            :>> scenarioType = MDOScenarioType::Mdo;

            :>> disciplines[4] = (SobieskiPropulsion, SobieskiAerodynamics, SobieskiMission, SobieskiStructure);

            #discipline part SobieskiPropulsion{
                // ---- Inputs ----
                in x_shared_0 : Real;
                in x_shared_1 : Real;
                in x_shared_2 : Real;
                in x_shared_3 : Real;
                in x_shared_4 : Real;
                in x_shared_5 : Real;

                in x_3 : Real;
                in y_22 : Real;   

                // ---- Outputs ----
                out y_31 : Real;  // Engine mass
                out y_32 : Real;  // SFC
                out y_33 : Real;  // ESF

                out g3_1 : Real;
                out g3_2 : Real;
                out g3_3 : Real;

                #mdaoModel part model : MDAOAnalyticalModel {
                     doc /* SobieskiPropulsion analytical model (see GEMSEO). */ 
                }
            }

            #discipline part SobieskiAerodynamics{
                // ---- Inputs ----
                in x_shared_0 : Real;
                in x_shared_1 : Real;
                in x_shared_2 : Real;
                in x_shared_3 : Real;
                in x_shared_4 : Real;
                in x_shared_5 : Real;

                in x_2 : Real;

                in y_11 : Real;   // W_T from Structure
                in y_13 : Real;   // Delta alpha_v from Structure
                in y_33 : Real;   // ESF from Propulsion

                // ---- Outputs ----
                out y_21 : Real;  // Lift
                out y_22 : Real;  // Drag
                out y_23 : Real;  // L/D

                out g2 : Real;

                #mdaoModel part model : MDAOAnalyticalModel {
                    doc /* SobieskiAerodynamics analytical model (see GEMSEO). */ 
                }
            }

            #discipline part SobieskiMission{
                // ---- Inputs ----
                in x_shared_0 : Real;
                in x_shared_1 : Real;
                in x_shared_2 : Real;
                in x_shared_3 : Real;
                in x_shared_4 : Real;
                in x_shared_5 : Real;

                in y_23 : Real;  // L/D from Aerodynamics
                in y_11 : Real;  // W_T from Structure
                in y_12 : Real;  // W_F from Structure
                in y_32 : Real;  // SFC from Propulsion

                // ---- Output ----
                out y_4 : Real;  // Range (objective)

                #mdaoModel part model : MDAOAnalyticalModel {
                     doc /* SobieskiMission analytical model (see GEMSEO). */ 
                }
            }

            #discipline part SobieskiStructure{
                 // ---- Inputs ----
                // Design variables
                in x_shared_0 : Real;
                in x_shared_1 : Real;
                in x_shared_2 : Real;
                in x_shared_3 : Real;
                in x_shared_4 : Real;
                in x_shared_5 : Real;

                in x_1_0 : Real;
                in x_1_1 : Real;

                // Couplings
                in y_21 : Real;   // Lift from Aerodynamics
                in y_31 : Real;   // Engine mass from Propulsion

                // ---- Outputs ----
                out y_11 : Real;  // W_T
                out y_12 : Real;  // W_F
                out y_13 : Real;  // Delta alpha_v

                // Constraint outputs (7)
                out g1_1 : Real;
                out g1_2 : Real;
                out g1_3 : Real;
                out g1_4 : Real;
                out g1_5 : Real;
                out g1_6 : Real;
                out g1_7 : Real;

                #mdaoModel part analyticalModel : MDAOAnalyticalModel { 
                    doc /* SobieskiStructure analytical model (see GEMSEO). */ 
                }
            }

            // Structure <-> Aerodynamics
            bind SobieskiStructure.y_11 = SobieskiAerodynamics.y_11;
            bind SobieskiStructure.y_13 = SobieskiAerodynamics.y_13;
            bind SobieskiAerodynamics.y_21 = SobieskiStructure.y_21;

            // Structure <-> Propulsion
            bind SobieskiPropulsion.y_31 = SobieskiStructure.y_31;

            // Aero <-> Propulsion
            bind SobieskiAerodynamics.y_22 = SobieskiPropulsion.y_22;
            bind SobieskiPropulsion.y_33 = SobieskiAerodynamics.y_33;

            // Mission couplings
            bind SobieskiAerodynamics.y_23 = SobieskiMission.y_23;
            bind SobieskiStructure.y_11 = SobieskiMission.y_11;
            bind SobieskiStructure.y_12 = SobieskiMission.y_12;
            bind SobieskiPropulsion.y_32 = SobieskiMission.y_32;

            :>> optimizer {
                perform action tmsOptimizationAlgorithm :>> optimizationAlgorithm {
                    :>> method:OptimizationMethod = OptimizationMethod::GradientBase;
                    :>> algorithmName:OptimizationAlgorithmType = OptimizationAlgorithmType::Slsqp;
                    :>> maxIter = 100;
                }
            }

            :>> converger {
                :>> convergenceMethod:ConvergenceMethod = ConvergenceMethod::GaussSeidel;
            }

            perform action sobieskiStatistics :>> statistics {
                :>> statisticType = StatisticType::Mean;
                :>> estimationSettings : SamplingEstimationSettings {
                    :>> sampleSize = 20;
                }
            }

            perform action sobieskiSampling :>> sampling {
                :>> method       = SamplingMethod::MonteCarlo;
                :>> nSamples     = 20;
                :>> isSequential = false;
            }
        }

        part sobieskiSolution :> mdaoSolution, sobieskiProblem{

        }

        // ============================================================
        // Bind Design Variables → Structure
        // ============================================================

        bind sobieskiProblem.x_shared_0 = sobieskiStrategy.SobieskiStructure.x_shared_0;
        bind sobieskiProblem.x_shared_1 = sobieskiStrategy.SobieskiStructure.x_shared_1;
        bind sobieskiProblem.x_shared_2 = sobieskiStrategy.SobieskiStructure.x_shared_2;
        bind sobieskiProblem.x_shared_3 = sobieskiStrategy.SobieskiStructure.x_shared_3;
        bind sobieskiProblem.x_shared_4 = sobieskiStrategy.SobieskiStructure.x_shared_4;
        bind sobieskiProblem.x_shared_5 = sobieskiStrategy.SobieskiStructure.x_shared_5;

        bind sobieskiProblem.x_1_0 = sobieskiStrategy.SobieskiStructure.x_1_0;
        bind sobieskiProblem.x_1_1 = sobieskiStrategy.SobieskiStructure.x_1_1;

        // ============================================================
        // Bind Design Variables → Aerodynamics
        // ============================================================

        bind sobieskiProblem.x_shared_0 = sobieskiStrategy.SobieskiAerodynamics.x_shared_0;
        bind sobieskiProblem.x_shared_1 = sobieskiStrategy.SobieskiAerodynamics.x_shared_1;
        bind sobieskiProblem.x_shared_2 = sobieskiStrategy.SobieskiAerodynamics.x_shared_2;
        bind sobieskiProblem.x_shared_3 = sobieskiStrategy.SobieskiAerodynamics.x_shared_3;
        bind sobieskiProblem.x_shared_4 = sobieskiStrategy.SobieskiAerodynamics.x_shared_4;
        bind sobieskiProblem.x_shared_5 = sobieskiStrategy.SobieskiAerodynamics.x_shared_5;

        bind sobieskiProblem.x_2 = sobieskiStrategy.SobieskiAerodynamics.x_2;


        // ============================================================
        // Bind Design Variables → Propulsion
        // ============================================================

        bind sobieskiProblem.x_shared_0 = sobieskiStrategy.SobieskiPropulsion.x_shared_0;
        bind sobieskiProblem.x_shared_1 = sobieskiStrategy.SobieskiPropulsion.x_shared_1;
        bind sobieskiProblem.x_shared_2 = sobieskiStrategy.SobieskiPropulsion.x_shared_2;
        bind sobieskiProblem.x_shared_3 = sobieskiStrategy.SobieskiPropulsion.x_shared_3;
        bind sobieskiProblem.x_shared_4 = sobieskiStrategy.SobieskiPropulsion.x_shared_4;
        bind sobieskiProblem.x_shared_5 = sobieskiStrategy.SobieskiPropulsion.x_shared_5;

        bind sobieskiProblem.x_3 = sobieskiStrategy.SobieskiPropulsion.x_3;


        // ============================================================
        // Bind Design Variables → Mission
        // ============================================================

        bind sobieskiProblem.x_shared_0 = sobieskiStrategy.SobieskiMission.x_shared_0;
        bind sobieskiProblem.x_shared_1 = sobieskiStrategy.SobieskiMission.x_shared_1;
        bind sobieskiProblem.x_shared_2 = sobieskiStrategy.SobieskiMission.x_shared_2;
        bind sobieskiProblem.x_shared_3 = sobieskiStrategy.SobieskiMission.x_shared_3;
        bind sobieskiProblem.x_shared_4 = sobieskiStrategy.SobieskiMission.x_shared_4;
        bind sobieskiProblem.x_shared_5 = sobieskiStrategy.SobieskiMission.x_shared_5;
    }

}