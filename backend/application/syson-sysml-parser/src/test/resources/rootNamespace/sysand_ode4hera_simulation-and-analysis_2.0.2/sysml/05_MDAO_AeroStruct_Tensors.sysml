package MDAO_AeroStruct_Tensors {

   
    private import ScalarValues::*;
    private import SI::*;
    private import TensorVectorMatrix::*; 
    private import MultidisciplinaryDesignAnalysisOptimization::*;
    private import ArchitectureDesign::*;

    #mdaoProcessArchitecture part MDAO_AeroStruct_Tensors{

        part AeroStructProblem :>> mdaoProblem {

            :>> designVariables = (thicknessVector, panelThicknessGrid);
          
            // Thickness at 5 span stations (vector view later)
            #contX attribute t1 : Real default 0.012 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute t2 : Real default 0.011 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute t3 : Real default 0.010 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute t4 : Real default 0.009 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute t5 : Real default 0.008 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }

            // Panel thickness grid (5 stations x 2 panels)
            #contX attribute T11 : Real default 0.012 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute T12 : Real default 0.012 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute T21 : Real default 0.011 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute T22 : Real default 0.011 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute T31 : Real default 0.010 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute T32 : Real default 0.010 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute T41 : Real default 0.009 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute T42 : Real default 0.009 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute T51 : Real default 0.008 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }
            #contX attribute T52 : Real default 0.008 ['m'] { :>> lowerBound = 0.005; :>> upperBound = 0.050; }

        
            #minObj attribute dragProxy;
            :>> objectives[1] = (dragProxy);

            :>> constraints = (stressMarginConstraint, tipDeflectionConstraint);

           
            #con attribute stressMarginConstraint{
                attribute stressMargin:Real;
                assert constraint { stressMargin >= 0 }
            }
            #con attribute tipDeflectionConstraint{
                attribute tipDeflection:Real;
                assert constraint { tipDeflection <= 15.0 }
            }

            
            // DesignVector usage: thickness distribution t[i]
            attribute thicknessVector : DesignVariablesVector {
                :>> tensor.shape = (5);
                :>> tensor.layout = TensorVectorMatrix::TensorLayout::RowMajor;
                :>> tensor.dimensionLabels = ("spanStation");
                :>> tensor.dimensionSemantics = ("ThicknessDistribution");
                :>> values = (t1, t2, t3, t4, t5);
            }

           
            // DesignMatrix usage: panel thickness grid T[i,j] with shape (5,2)
            // i = span station, j = panel id (1=front, 2=rear)
            attribute panelThicknessGrid : DesignVariablesMatrix {
                :>> tensor.shape = (5,2);
                :>> tensor.layout = TensorVectorMatrix::TensorLayout::RowMajor;
                :>> tensor.dimensionLabels = ("station","panel");
                :>> tensor.dimensionSemantics = ("SpanStation","PanelId");
                :>> values = ((T11, T12), (T21, T22), (T31, T32), (T41, T42), (T51, T52));
            }

            // Register tensor views in the MDAOProblemDefinition
            :>> vectors  = (thicknessVector);
            :>> matrices = (panelThicknessGrid);
        }

 
        part def AeroStructStrategy :> MDAOStrategyDefinition {

            :>> mdoFormulation = MDOFormulation::Mdf;
            :>> scenarioType   = MDOScenarioType::Mdo;

            doc
            /*
            * MDF strategy with coupled aero <-> structures.
            * Disciplines use scalar interfaces (tool-friendly),
            * while the problem provides vector/matrix structural views for export.
            */

            #discipline part AerodynamicsDiscipline {
                
                in t1 : Real; 
                in t2 : Real; 
                in t3 : Real; 
                in t4 : Real; 
                in t5 : Real;
                in T11 : Real; 
                in T12 : Real; 
                in T21 : Real; 
                in T22 : Real; 
                in T31 : Real; 
                in T32 : Real; 
                in T41 : Real; 
                in T42 : Real; 
                in T51 : Real; 
                in T52 : Real;


                #mdaoParameter attribute mach  : ISQCharacteristicNumbers::MachNumberValue default 0.78 * ISQCharacteristicNumbers::machNumber;
                #mdaoParameter attribute alpha : ISQSpaceTime::PlaneAngleValue default 2.0 ['rad'];
                #mdaoParameter attribute rho   : ISQMechanics::MassDensityValue default 0.38['kg⋅m⁻³'];
                #mdaoParameter attribute V     : ISQSpaceTime::SpeedValue default 230.0 ['m/s'];

                // Parameter vector concept (scenario): kept as structural TensorVector (optional)
                #mdaoParameter attribute flightScenario : TensorVector {
                    :>> tensor.shape = (4);
                    :>> tensor.dimensionLabels = ("scenario");
                    :>> tensor.dimensionSemantics = ("FlightCondition");
                    :>> values = (mach, alpha, rho, V);
                }

                // Outputs
                out lift : Real;
                out dragProxy : Real;
                out spanLoad1 : Real; out spanLoad2 : Real; out spanLoad3 : Real; out spanLoad4 : Real; out spanLoad5 : Real;
            }

            #discipline part StructuresDiscipline {
                // Inputs: loads + thickness design variables
                in spanLoad1 : Real; in spanLoad2 : Real; in spanLoad3 : Real; in spanLoad4 : Real; in spanLoad5 : Real;
                in T11 : Real; in T12 : Real; in T21 : Real; in T22 : Real; in T31 : Real; in T32 : Real; in T41 : Real; in T42 : Real; in T51 : Real; in T52 : Real;

                // Outputs
                out stressMargin : Real;
                out tipDeflection : Real;
            }

            #discipline part PerformanceDiscipline {
                in dragProxy : Real;
                out objective_drag : Real;
            }

            :>> disciplines = (AerodynamicsDiscipline, StructuresDiscipline, PerformanceDiscipline);


            part aeroStructOptimizer :>> optimizer {

                perform action aeroStructOptimizationAlgorithm :>> optimizationAlgorithm {

                    :>> method = OptimizationMethod::GradientBase;
                    :>> algorithmName = OptimizationAlgorithmType::Slsqp;
                    :>> maxIter = 500;
                }
            }
        }

        part def AeroStructSolution :> MDAOSolutionDefinition { }

    }
}
