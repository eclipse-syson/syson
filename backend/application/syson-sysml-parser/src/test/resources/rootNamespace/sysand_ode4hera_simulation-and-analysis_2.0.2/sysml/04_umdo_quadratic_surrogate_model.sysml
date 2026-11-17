package UMDO_Quadratic_Surrogate_Example {
    doc /* Quadratic mono-disciplinary UMDO problem with surrogate-based
        statistic estimation, following:

        https://gemseo.gitlab.io/dev/gemseo-umdo/latest/generated/examples/umdo/surrogate/plot_surrogate/

        Problem:
            minimize   E[(x+U)^2]
            over       x in [-1, 1]
            with       U ~ Normal(0,1)

        Implemented as:
            - one analytic discipline y = (x+u)^2
            - design variable x
            - uncertain variable u
            - statistic: Mean(y)
            - estimator: surrogate-based with n_samples = 20 (Surrogate_Settings)
            - optimizer: COBYLA (gradient-free)

        Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
        */

    private import ArchitectureDesign::*;
    private import MultidisciplinaryDesignAnalysisOptimization::*;
    private import MultidisciplinaryDesignAnalysisOptimization::Uncertainty::*;
    private import ScalarValues::*;
    private import ProbabilityAndSampling::*;

    #umdaoProcessArchitecture part QuadraticUMDO {

        part quadraticProblem :>> umdaoProblem {

            :>> designVariables[1] = (x);

            #contX attribute x : Real default 0.5 {
                doc /* Design variable x in [-1,1], initial value 0.5. */
                redefines lowerBound = -1.0;
                redefines upperBound =  1.0;
            }

            :>> uncertainVariables[1] = (u);

            #uncertainVariable attribute u {
                doc /* Standard Gaussian random variable U ~ N(0,1). */

                :>> distribution :> NormalDistribution {
                    :>> mean   = 0.0;
                    :>> stdDev = 1.0;
                }
            }

            :>> objectives[1] = quadraticObjective;

            #minObj attribute quadraticObjective {
                doc /* Objective: minimize the mean of y = (x+U)^2. */
            }

        }

        part quadraticStrategy :> umdaoStrategy {

            :>> disciplines[1] = (QuadraticDiscipline);

            #discipline part QuadraticDiscipline {
                in  x : Real;
                in  u : Real;
                out y : Real;

                #mdaoModel part analyticalModel : MDAOAnalyticalModel {
                    doc
                    /*
                     * Analytic discipline f:
                     *   y = (x + u)**2
                     *
                     * Inputs:
                     *   x: design variable
                     *   u: uncertain variable ~ N(0,1)
                     */
                }
            }

            :>> mdoFormulation = MDOFormulation::Aao;

            part quadraticOptimizer :>> optimizer {
                perform action quadraticOptimizationAlgorithm :>> optimizationAlgorithm {
                    :>> method : OptimizationMethod = OptimizationMethod::GradientFree;
                    :>> algorithmName : OptimizationAlgorithmType = OptimizationAlgorithmType::Cobyla;
                    :>> maxIter = 100;
                }
            }

            part dummyConverger :>> converger {
                :>> convergenceMethod : ConvergenceMethod = ConvergenceMethod::RunOnce;
            }

            perform action quadraticStatistics :>> statistics {
                doc /* Statistic of interest:
                        Mean(y), estimated via a surrogate model.

                          statistic_name = "Mean"
                          statistic_estimation_settings = Surrogate_Settings(n_samples=20)

                        which means:
                          - train a surrogate of y(x,u)
                          - use 20 samples per iteration to estimate E[y] from the surrogate.
                     */

                :>> statisticType = StatisticType::Mean;

                :>> estimationSettings : SurrogateEstimationSettings {
                    :>> nTrainingSamples = 20;
                }
            }

        }

        part quadraticSolution :> mdaoSolution, quadraticProblem {

            :>> designVariablesSolution[1] = (x);
            :>> x : Real = 0.0 {
                doc /* Optimal x*, expected to be close to 0.0. */
            }

            :>> objectiveFunctionSolution[1] = (quadraticObjective);
            :>> quadraticObjective : Real = 1.0 {
                doc /* Optimal mean objective value E[(x*+U)^2], close to 1.0. */
            }
        }
    }
}
