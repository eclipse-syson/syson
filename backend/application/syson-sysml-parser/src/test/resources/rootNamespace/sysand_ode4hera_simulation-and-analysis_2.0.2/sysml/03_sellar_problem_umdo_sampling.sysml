package MDAO_SellarProblem_UMDO_Samplin_Example {
    doc /* The classic MDAO Sellar test problem for UMDO.
    For example: https://gemseo.gitlab.io/dev/gemseo-umdo/latest/generated/examples/umdo/sampling/s_sellar/
    
    Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
    */
    
    private import ArchitectureDesign::*;
    private import MultidisciplinaryDesignAnalysisOptimization::*;
    private import MultidisciplinaryDesignAnalysisOptimization::Uncertainty::*;
    private import ScalarValues::*;
    private import ProbabilityAndSampling::*;

    #umdaoProcessArchitecture part UMDAOSellar {

        part umdaoSellarProblem :>> umdaoProblem {

            :>> deterministicVariables[3] = (x, z1, z2);

            #contX attribute x : Real default 1.0 {
                redefines lowerBound = 0.0;
                redefines upperBound = 10.0;
            }

            #contX attribute z1 : Real default 4.0 {
                redefines lowerBound = -10.0;
                redefines upperBound = 10.0;
            }

            #contX attribute z2 : Real default 3.0 {
                redefines lowerBound = 0.0;
                redefines upperBound = 10.0;
            }

            :>> uncertainVariables[1] = (a);

            #uncertainVariable attribute a {
                :>> distribution :> TriangularDistribution {
                    :>> min  = 0.1;
                    :>> mode = 0.2;
                    :>> max  = 0.3;
                }
            }

            :>> objectives[1] = sellarObjectiveFunction;

            #minObj attribute sellarObjectiveFunction;

            :>> constraints[2] = (constraint1, constraint2);

            #umdaoConstraint attribute constraint1 : Real {

                :>> uqConstraintType = UQConstraintType::Margin;
                :>> statisticType    = StatisticType::StandardDeviation;
                :>> marginFactor     = 3.0;

                assert constraint { that <= 0.0 }
            }

            #umdaoConstraint attribute constraint2 : Real {
                doc /* UMDO constraint:
                     *   Enforce E[c2(y2)] + 3*S[c2(y2)] <= 0
                     *   where c2(y2) = y2 - 24.0. */

                :>> uqConstraintType = UQConstraintType::Margin;
                :>> statisticType    = StatisticType::StandardDeviation;
                :>> marginFactor     = 3.0;

                assert constraint { that <= 0.0 }
            }
        }

        part umdaoSellarStrategyCV :> umdaoStrategy {

            :>> disciplines[3] = (SellarDiscipline1, SellarDiscipline2, SellarSystem);

            #discipline part SellarDiscipline1{
                in x : Real;
                in y2 : Real;
                in z1 : Real;
                in z2 : Real;
                in a : Real;
                out y1 : Real;
                #mdaoModel part analyticalModel:MDAOAnalyticalModel{
                    doc
                    /*
                    * y1 = (z1**2 + z2 + x - a*y2)**0.5
                    */
                }
            }
            
            #discipline part SellarDiscipline2{
                in y1: Real;
                in z1 : Real;
                in z2 : Real;
                out y2 : Real;
                #mdaoModel part analyticalModel:MDAOAnalyticalModel{
                    doc
                    /*
                    *  y2 = abs(y1) + z1 + z2
                    */
                }
            }
            #discipline part SellarSystem{ 
                in x : Real;
                in z2 : Real;
                in y1 : Real;
                in y2 : Real;
                out c1;
                out c2;
                out obj;
                #mdaoModel part analyticalModel:MDAOAnalyticalModel{
                    doc/*
                        * """Objective function."""
                        *    obj = x**2 + z2 + y1**2 + exp(-y2)
                        *    c1 = 3.16 - y1**2
                        *    c2 = y2 - 24.0
                        */
                }
            }

            bind SellarDiscipline1.y1 = SellarDiscipline2.y1;
            bind SellarDiscipline2.y2 = SellarDiscipline1.y2;
            
            :>> mdoFormulation = MDOFormulation::Mdf;

            part sellarOptimizer :>> optimizer {
                perform action sellarOptimizationAlgorithm :>> optimizationAlgorithm {
                    :>> method:OptimizationMethod = OptimizationMethod::GradientFree;
                    :>> algorithmName:OptimizationAlgorithmType = OptimizationAlgorithmType::Cobyla;
                    :>> maxIter = 200;
                }
            }

            part MDAGaussSeidelConverger :>> converger {
                :>> convergenceMethod:ConvergenceMethod = ConvergenceMethod::GaussSeidel;
            }

            perform action sellarStatistics :>> statistics {
                doc /* The UMDO scenario uses "Mean" as statistic_name
                     * for the objective, i.e. E[f(x,z2,y1,y2)]. */

                :>> statisticType = StatisticType::Mean;

                :>> estimationSettings : SamplingEstimationSettings {
                    :>> sampleSize = 100; 
                }
            }

            perform action sellarSampling :>> sampling {
                  doc /* Crude Monte Carlo sampling with 100 samples per
                     * optimization iteration, as in the UMDO sampling
                     * Sellar example. */

                :>> method      = SamplingMethod::MonteCarlo;
                :>> nSamples    = 100;
                :>> isSequential = false;
            }

        }

        part umdaoSellarSolutionCV :> mdaoSolution, umdaoSellarProblem {

            :>> designVariablesSolution[3] = (x, z1, z2);
            :>> x : Real;
            :>> z1 : Real;
            :>> z2 : Real;

            :>> objectiveFunctionSolution[1] = sellarObjectiveFunction;
            :>> sellarObjectiveFunction;

            :>> constraintsSolution[2] = (constraint1, constraint2);
            :>> constraint1;
            :>> constraint2;
        }
    }
}