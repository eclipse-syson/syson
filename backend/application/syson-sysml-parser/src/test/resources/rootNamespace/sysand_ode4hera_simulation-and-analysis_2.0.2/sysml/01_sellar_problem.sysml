package MDAO_SellarProblemExample {
    doc /* The classic MDAO Sellar test problem.
    For example: https://openmdao.org/newdocs/versions/latest/basic_user_guide/multidisciplinary_optimization/sellar.html
    
    Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
    */
    
    private import ArchitectureDesign::*;
    private import MultidisciplinaryDesignAnalysisOptimization::*;
    private import ScalarValues::*;

    #mdaoProcessArchitecture part SellarProcessArchitecture {
   
        part sellarProblem :>> mdaoProblem {

            :>> designVariables[5] = (x_shared_1, x_shared_2, x_local, y_1, y_2);
                
            #contX attribute x_shared_1 : Real {
                redefines lowerBound = 0; redefines upperBound = 10;   
            }
            #contX attribute x_shared_2 : Real {
                redefines lowerBound = 0; redefines upperBound = 10; 
            }

            #contX attribute x_local : Real {
                redefines lowerBound = 0; redefines upperBound = 10; 
            }

            #contX attribute y_1 : Real {
                redefines lowerBound = 100; redefines upperBound = 100; 
            }

            #contX attribute y_2 : Real {
                redefines lowerBound = 100; redefines upperBound = 100; 
            }

            :>> objectives[1] = sellarObjectiveFunction;

            #minObj attribute sellarObjectiveFunction;

            :>> constraints[2] = (constraint1, constraint2);

            #con attribute constraint1 : Real {
                assert constraint { that <= 0.0 }
            }
            #con attribute constraint2 : Real {
                assert constraint { that <= 0.0 }
            }       
        }
        
        part sellarStrategy :>> mdaoStrategy {

            :>> disciplines[3] = (SellarDiscipline1, SellarDiscipline2, SellarSystem);

            #discipline part SellarDiscipline1 :> disciplines {
                in x_local : Real;
                in y_2 : Real;
                in x_shared_1 : Real;
                in x_shared_2 : Real;
                out y_1 : Real;
                #mdaoModel part analyticalModel : MDAOAnalyticalModel {
                    doc
                    /*
                    * y_1 = x_shared_1**2 + x_shared_2 + x_local - 0.2*y_2
                    */
                }
            }
            
            #discipline part SellarDiscipline2 :> disciplines {
                in x_shared_1 : Real;
                in x_shared_2 : Real;
                in y_1 : Real;
                out y_2 : Real;
                #mdaoModel part analyticalModel : MDAOAnalyticalModel {
                    doc
                    /*
                    *  y_2 = y_1**.5 + x_shared_1 + x_shared_2
                    */
                }
            }
            #discipline part SellarSystem :> disciplines { 
                in x_local : Real;
                in x_shared_2 : Real;
                in y_1 : Real;
                in y_2 : Real;
                out c_1;
                out c_2;
                out obj;
                #mdaoModel part analyticalModel : MDAOAnalyticalModel {
                    doc/*
                        * """Objective function."""
                        *    obj = x_local**2 + x_shared_2 + y_1**2 + exp(-y_2)
                        *    c_1 = 3.16 - y_1**2
                        *    c_2 = y_2 - 24.0
                        */
                }
            }

            bind SellarDiscipline1.y_1 = SellarDiscipline2.y_1;
            bind SellarDiscipline2.y_2 = SellarDiscipline1.y_2;
            
            :>> mdoFormulation = MDOFormulation::Mdf;

            part sellarOptimizer :>> optimizer {
                perform action sellarOptimizationAlgorithm :>> optimizationAlgorithm {
                    :>> method:OptimizationMethod = OptimizationMethod::GradientFree;
                    :>> algorithmName:OptimizationAlgorithmType = OptimizationAlgorithmType::Slsqp;
                }
            }

            part MDAGaussSeidelConverger :>> converger {
                :>> convergenceMethod:ConvergenceMethod = ConvergenceMethod::GaussSeidel;
            }
        }


        part sellarSolution :> mdaoSolution, sellarProblem {

            :>> designVariablesSolution[3] = (x_local, x_shared_1, x_shared_2);
            :>> x_local : Real = 4.465562989269076e-15;
            :>> x_shared_1 : Real = 1.97763887873791;
            :>> x_shared_2 : Real = 0;

            :>> objectiveFunctionSolution[1] = sellarObjectiveFunction;
            :>> sellarObjectiveFunction = 3.183393951751462;

            :>> constraintsSolution[2] = (constraint1, constraint2);
            :>> constraint1 = -3.086420008457935e-13;
            :>> constraint2 = -20.244722237798886;
        }

        bind sellarProblem.x_local = sellarStrategy.SellarDiscipline1.x_local;
        bind sellarProblem.x_shared_1 = sellarStrategy.SellarDiscipline1.x_shared_1;
        bind sellarProblem.x_shared_1 = sellarStrategy.SellarDiscipline2.x_shared_1;
        bind sellarProblem.x_shared_2 = sellarStrategy.SellarDiscipline1.x_shared_2;
        bind sellarProblem.x_shared_2 = sellarStrategy.SellarDiscipline2.x_shared_2;
        bind sellarProblem.x_local = sellarStrategy.SellarSystem.x_local;
        bind sellarProblem.x_shared_1 = sellarStrategy.SellarSystem.x_shared_2;

        bind sellarStrategy.SellarDiscipline1.y_1 = sellarStrategy.SellarSystem.y_1;
        bind sellarStrategy.SellarDiscipline2.y_2 = sellarStrategy.SellarSystem.y_2;
        bind sellarStrategy.SellarDiscipline1.y_1 = sellarStrategy.SellarSystem.y_1;
        bind sellarStrategy.SellarDiscipline2.y_2 = sellarStrategy.SellarSystem.y_2;
        
        bind sellarStrategy.SellarSystem.obj = sellarProblem.sellarObjectiveFunction;
        bind sellarStrategy.SellarSystem.c_1 = sellarProblem.constraint1;
        bind sellarStrategy.SellarSystem.c_2 = sellarProblem.constraint2;
    }
}