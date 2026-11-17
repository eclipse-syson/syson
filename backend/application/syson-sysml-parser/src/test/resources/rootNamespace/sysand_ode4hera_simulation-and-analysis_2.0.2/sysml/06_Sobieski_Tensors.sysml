package Sobieski_Tensors_Example {
    doc /* The Sobieski's Super-Sonic Business Jet problem for Tensors illustration.
    From: https://gemseo.readthedocs.io/en/stable/examples/mdo/plot_sobieski_use_case.html
    
    Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
    */

    private import SI::*;
    private import TensorVectorMatrix::*;
    private import RealFunctions::*;
    private import MultidisciplinaryDesignAnalysisOptimization::*;
    private import ArchitectureDesign::*;

    #mdaoProcessArchitecture part SobieskiMDAO{

        part sobieskiProblem :>> mdaoProblem{
            doc
            /*
             * GEMSEO Sobieski design variables are vector-valued:
             * - x_shared has 6 components
             * - x_1 has 2 components (structure local)
             * - x_2 has 1 component (aero local)
             * - x_3 has 1 component (propulsion local)
             *
             * We keep scalar DVs for tool-friendly typing, and add vector/matrix views
             * using the TensorVectorMatrix library (values = (...) and values = ((...),(...))).
             */

            :>> designVariables = (x_shared, x_1, x_2, x_3);
            
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

            :>> objectives[1] = y_4;

            #minObj attribute y_4;
           
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

            #con attribute g1: TensorVector {
                
                :>> tensor.shape = (7);
                :>> tensor.dimensionLabels = ("k");
                :>> tensor.dimensionSemantics = ("StructureConstraintIndex");
                :>> values = (g1_1, g1_2, g1_3, g1_4, g1_5, g1_6, g1_7);
                
                assert constraint { values <= 0.0 }
            }

            #con attribute g2: Real {
                assert constraint { that <= 0.0 }
            }

            #con attribute g3 : TensorVector {             
                :>> tensor.shape = (3);
                :>> tensor.dimensionLabels = ("k");
                :>> tensor.dimensionSemantics = ("PropulsionConstraintIndex");
                :>> values = (g3_1, g3_2, g3_3);      
                assert constraint { values <= 0.0 }
            }

        }

        part sobieskiStrategy :>> mdaoStrategy {
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
                    :>> method:OptimizationMethod = OptimizationMethod::GradientFree;
                    :>> algorithmName:OptimizationAlgorithmType = OptimizationAlgorithmType::Slsqp;
                    :>> maxIter = 10;
                }
            }

            :>> converger {
                :>> convergenceMethod:ConvergenceMethod = ConvergenceMethod::GaussSeidel;
                :>> residualEpsilon = 1e-14;
                :>> maxMdaIter = 50;
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