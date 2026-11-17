//
// Multidisciplinary Design Analysis and Optimization library
//
// Library for modeling Multidisciplinary Design Analysis and Optimization (MDAO) concepts.
//
// This library has been developed as part of the ODE4HERA project: https://www.ode4hera.eu/
//
// Version: v2.0
// License: MIT
// Maintainer: DLR Institute of System Architectures in Aeronautics
// Contact: digital.engineering@dlr.de
// Topics: MDAO, Multidisciplinary Design Analysis and Optimization
//
// MIT License
//
// Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

library package MultidisciplinaryDesignAnalysisOptimization {
	doc
    /*
	 * Domain-specific library implementing Multidisciplinary Design Analysis and Optimization (MDAO) capabilities:
     * - Design variables, objectives, constraints
     * - MDAO data model
     * - Disciplinary tools/models
     * - Disciplinary inputs and outputs
     * - Analysis process
     * - Convergers
     * - Results of the process
     *   - Single value when mono objective 
     *   - Pareto front for multi objective (several design value)
     *   - Gradients values
     *   - nb of iterations
     *
     * Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
     */

    private import ScalarValues::*;
    private import Collections::*;
    private import RequirementsManagement::*;
    private import ModelBasedDesign::*;
    private import ArchitectureDesign::*;
    public import MDAOSemanticMetadata::*;
    private import TensorVectorMatrix::*;


    abstract part def MDAOComponent {
        ref requirement tracedRequirement[0..*];
    }
    abstract action def MDAOAction {
        ref requirement tracedRequirement[0..*];
    }

    abstract part def MDAOGroup {
        abstract action mdaoAction[1..*] : MDAOAction;
        abstract part mdaoComponents[1..*] : MDAOComponent;
    }

    part def MDAOProcessArchitectureDefinition {
        
        ref part systemUnderAnalysis : Architecture;

        part stakeholderThatNeedsMDAO : Stakeholder;
        attribute stakeholderMDAOObjective : String;
        
        abstract part mdaoGroup : MDAOGroup;
        part mdaoProblem[1] : MDAOProblemDefinition :> mdaoGroup;
        part mdaoStrategy[1] : MDAOStrategyDefinition :> mdaoGroup;
        part mdaoSolution[1] : MDAOSolutionDefinition :> mdaoGroup;
    }
    
    part mdaoArchitecture[*] : MDAOProcessArchitectureDefinition;
 
    part def MDAOProblemDefinition :> MDAOGroup, FormalDesignProblem{
        attribute vectors : DesignVariablesVector[0..*];
        attribute matrices : DesignVariablesMatrix[0..*];
    }
   
    part def MDAOStrategyDefinition :> MDAOGroup {
        attribute mdoFormulation[1] : MDOFormulation;
        attribute scenarioType[1] : MDOScenarioType;
        
        part disciplines[1..*] : DisciplineDefinition :> mdaoComponents;
        part optimizer[1] : OptimizerDefinition :> mdaoComponents;
        part converger[0..1] : MDAConvergerDefinition :> mdaoComponents;
    }
    
    part def MDAOSolutionDefinition :> MDAOProblemDefinition {
        attribute designVariablesSolution :>> designVariables;
        attribute objectiveFunctionSolution :>> objectives;
        attribute constraintsSolution :>> constraints;
    }


    attribute def DesignVariablesVector :> TensorVector{
        doc
        /*
        * Vector specialized for design variables.
        */
        :>> values :> ArchitectureDesign::DesignVariable[1..*];
    }

    attribute def DesignVariablesMatrix :> TensorMatrix{
         doc
        /*
        * Vector specialized for design variables.
        */
        :>> values :> ArchitectureDesign::DesignVariable[1..*];
    }

    part def DisciplineDefinition :> MDAOComponent {
        doc
        /*
        * A discipline that is a container of a simulation object and traced to specific requirements.
        * Can be a nested hierarchical optimization problem as for bi-level for instance
        */
        attribute disciplineName : ScalarValues::String; //can be beneficial
        part mdaoModels[0..*] : MDAOModelDefinition;
        part subProcessArchitectureDefinition[0..1] : MDAOProcessArchitectureDefinition;
        
        out stateVariables[*] :> stateVariable;
        attribute parameters: ParameterDefinition[0..*];
    }

    part def MDAOModelDefinition;

    part def MBDModel :> MDAOModelDefinition {
        ref part simulationObject[1..1] : MBDContainer; 
    }

    part def MDAOAnalyticalModel :> MDAOModelDefinition {
      
    }

    part def SurrogateModel :> MBDModel;
    
    part def OptimizerDefinition :> MDAOComponent {
        perform action optimizationAlgorithm[1..1] : OptimizationAlgorithmDefinition;
    }
    
    part def MDAConvergerDefinition {
        doc /* Base class for defining a converger */
        attribute convergenceMethod[1..1] : ConvergenceMethod;
        attribute residualEpsilon[0..1] : Real;
        attribute maxMdaIter[0..1] : Real;
    }

    attribute stateVariable{
        ref requirement tracedRequirement[0..*];
    }


    attribute def ParameterDefinition :> Quantities::TensorQuantityValue;
    

    action def OptimizationAlgorithmDefinition :> MDAOAction{
        attribute maxIter[0..1] : ScalarValues::Integer;
        attribute method : OptimizationMethod;
        attribute algorithmName : OptimizationAlgorithmType;
    }
    
    abstract action def PerformConvergence{
        doc /* Base class for defining a converger */
    }
    
    action def MDAConvergence :> PerformConvergence {
        
    }
    
    enum def MDOFormulation{
        Mdf;
        Idf;
        BiLevel;
        Co;
        Bliss;
        Bliss2000;
        Aao;
    }
    
    enum def MDOScenarioType{
        Mdo;
        Doe;
    }

    enum def OptimizationMethod{
        GradientBase;
        GradientFree;
    }

    enum def OptimizationAlgorithmType{
        Slsqp;
        Cobyla;
    }
    
    enum def ConvergenceMethod{
        GaussSeidel;
        Jacobi;
        Newton;
        Broyden;
        RunOnce;
    }
    
    package MDAOSemanticMetadata {
        metadata def mdaoProcessArchitecture :> Metaobjects::SemanticMetadata {
            :> annotatedElement: SysML::PartUsage;
            :>> baseType = mdaoArchitecture meta SysML::PartUsage;
        }
        metadata def discipline :> Metaobjects::SemanticMetadata {
            :> annotatedElement : SysML::PartUsage;
            :>> baseType = MDAOStrategyDefinition::disciplines meta SysML::PartUsage;
        }
        metadata def stateVariable :> Metaobjects::SemanticMetadata {
            :> annotatedElement : SysML::Feature;
            :>> baseType = DisciplineDefinition::stateVariables meta SysML::Feature;
        }
        metadata def mdaoParameter :> Metaobjects::SemanticMetadata {
            :> annotatedElement : SysML::AttributeUsage;
            :>> baseType = DisciplineDefinition::parameters meta SysML::AttributeUsage;
        }
        metadata def mdaoModel :> Metaobjects::SemanticMetadata {
            :> annotatedElement : SysML::PartUsage;
            :>> baseType = DisciplineDefinition::mdaoModels meta SysML::PartUsage;
        }
    }

    package DesignOfExperiment{
        
        part def DesignOfExperimentDef{
            perform action doeAlgorithm[1..1]:DOEAlgorithmDefinition;
        }

        action def DOEAlgorithmDefinition :> MDAOAction{
            attribute algorithmName:DOEAlgorithmType;
        }

        enum def DOEAlgorithmType {
            MonteCarlo;
            Sobol;
            Faure;
            Halton; 
            Haselgrove;
            ReverseHalton;
            LHS;
            LHSC;
            Fullfact;
            Ff2n;
            Factorial;
            Axial;
            Composite;
            Ccdesign;
            Boxbehnkendesign;
            Plackettburmandesign;
        }
    }

    package Uncertainty{
        private import ControlFunctions::*;
        private import ProbabilityAndSampling::*;
        public import UMDAOSemanticMetadata::*;

        part def UMDAOProcessArchitectureDefinition :> MDAOProcessArchitectureDefinition {
            part umdaoProblem[1] : UMDAOProblemDefinition :>> mdaoProblem;
            part umdaoStrategy[1] : UMDAOStrategyDefinition :>> mdaoStrategy;
        }
        part umdaoArchitecture[*] : UMDAOProcessArchitectureDefinition;

        part def UMDAOProblemDefinition :> MDAOProblemDefinition {
            doc /* Variable space defining uncertain and deterministic variables.
            Deterministic variables are specified only by their bounds.
            Uncertain variables reference a probability distribution that 
            characterizes their variability. */

            attribute deterministicVariables[0..*] :> DeterministicVariable :>> designVariables;

            attribute uncertainVariables[1..*] :> UncertainVariable;
        }

        attribute UMDAOConstraintDefinition :> ConstraintOutput {
            doc /* A UMDAO constraint combines deterministic and uncertainty-related
                semantics. It can represent deterministic constraints, margin-type
                constraints (E[c] + k * StdDev[c] <= 0), or chance constraints. */

            attribute uqConstraintType : UQConstraintType default UQConstraintType::Deterministic {
                doc /* Type of UQ-aware constraint (Deterministic, Margin, Chance). */
            }

            attribute statisticType : StatisticType default StatisticType::Mean {
                doc /* Statistic used for margin evaluation: typically Mean or Variance
                    or StandardDeviation depending on uqConstraintType. */
            }

            attribute marginFactor[0..1] : ScalarValues::Real {
                doc /* The factor “k” in a margin-type constraint:
                        Mean[c] + k * StdDev[c] <= 0.
                    Only applicable when uqConstraintType == Margin. */
            }
        }

        part def UMDAOStrategyDefinition :> MDAOStrategyDefinition {
            doc /* UMDO-specific strategy definition extending a classical
                MDAO strategy with uncertainty propagation and statistics
                estimation aspects. */

            perform action statistics[1..1] : StatisticsDefinition;

            perform action sampling[0..1] : SamplingDefinition {
                doc /* Optional sampling-based uncertainty propagation
                    definition associated with this UMDO strategy. */
            }

            part controlVariates[0..*] : ControlVariateDefinition {
                doc /* Optional set of control variates used to reduce the
                    variance of sampling-based estimators (e.g. Monte Carlo,
                    multilevel Monte Carlo). */
            }
        }

        part def ControlVariateDefinition :> MDAOComponent {
            doc /* Definition of a control variate used as a variance reduction
                technique for statistics estimation.

                A control variate is a cheap model output that is correlated
                with the quantity of interest and whose statistics are easier
                to estimate. It is used to reduce the variance of Monte Carlo
                estimators. */

            ref part primaryModel[1] : MDAOModelDefinition {
                doc /* The original (potentially expensive) model whose outputs
                    define the quantity of interest for UMDO. */
            }

            ref part controlModel[1] : MDAOModelDefinition {
                doc /* The cheap surrogate or reduced model used as control
                    variate, correlated with the primary model outputs. */
            }

            attribute knownMean[0..*] : ScalarValues::Real {
                doc /* Optional known expectations of selected control model
                    outputs. When available, they can be used to build
                    unbiased control variate estimators. */
            }

            attribute isMultiLevel[0..1] : ScalarValues::Boolean default false {
                doc /* True if this control variate arises from a multi-fidelity
                    or multilevel hierarchy of models (e.g. coarse vs. fine
                    solver levels). */
            }
        }

        attribute DeterministicVariable :> ContinuousDesignVariable : ScalarValues::Real {
            doc /* A deterministic variable represents a controlled, non-random input.
            It is defined by a real value constrained within lower/upper bounds. */
        }

        // Base uncertainty variable
        attribute UncertainVariable : Base::DataValue {
            doc /* An uncertain variable represents a random or unknown input of the system.
            Its variability is governed by a probability distribution. */

            attribute distribution[1] :> ProbabilityDistribution {
                doc /* The probability distribution governing the randomness of the uncertain variable. */
            }
        }

        enum def UQConstraintType {
            Deterministic;   /* Classical c(x) <= 0 */
            Margin;          /* Mean[c] + k * StdDev[c] <= 0 */
            Chance;          /* P(c(x,U) <= 0) >= p */
        }

        package UMDAOSemanticMetadata {
            metadata def umdaoProcessArchitecture :> Metaobjects::SemanticMetadata {
                :> annotatedElement : SysML::PartUsage;
                :>> baseType = umdaoArchitecture meta SysML::PartUsage;
            }
            
            metadata def uncertainVariable :> Metaobjects::SemanticMetadata {
                :> annotatedElement : SysML::AttributeUsage;
                :>> baseType = UncertainVariable meta SysML::AttributeUsage;
            }
            
            metadata def umdaoConstraint :> Metaobjects::SemanticMetadata {
                :> annotatedElement : SysML::AttributeUsage;
                :>> baseType = UMDAOConstraintDefinition meta SysML::AttributeUsage;
            }
        }
    }
}
