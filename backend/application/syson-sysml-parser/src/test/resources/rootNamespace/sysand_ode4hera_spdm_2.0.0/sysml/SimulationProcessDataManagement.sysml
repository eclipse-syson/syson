//
// Simulation and Analysis library
//
// Library for modeling Simulation Process Data Management (SPDM) concepts.
//
// This library has been developed as part of the ODE4HERA project: https://www.ode4hera.eu/
//
// Version: v2.0
// License: MIT
// Maintainer: DLR Institute of System Architectures in Aeronautics
// Contact: jasper.bussemaker@dlr.de, francesco.torrigiani@dlr.de, luca.boggero@dlr.de
// Topics: SPDM, Simulation Process Data Management
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

library package SimulationProcessDataManagement {
	doc
    /*
	 * Domain-specific library implementing Simulation Process Data Management capabilities.
     *
     * Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
	 */

    private import ScalarValues::*;
    private import RequirementsManagement::*;
    private import ArchitectureDesign::*;
    private import ModelBasedDesign::*;
    private import MultidisciplinaryDesignAnalysisOptimization::*;

    // Common value and result aliases
    alias Parameter for Quantities::TensorQuantityValue;

    alias RawCompleteSimulationResult for Base::DataValue {
        doc /* Any logged values, characters and graphs a simulation application produces during runtime. */
    }

    alias SelectedSimulationResult for RawCompleteSimulationResult {
        doc /* Any logged value, character and graph a simulation application has produced during runtime and which the user has selected as relevant to be uploaded back to the SPDM system. */
    }

    analysis def Request {
        doc /* A PLM-managed message sent to ask for a certain task to be done. */
        attribute requestType : RequestType;
        part simulationStrategy : SimulationStrategy;
        subject simulationObject : SimulationObject;
        objective selectedRequirement [*];
        ref attribute selectedSimulationResult : SelectedSimulationResult;
        return verificationStatus : VVStatus;
    }

    part def SimulationStrategy {
        doc /* PLM container which hosts a simulation strategy (for instance, an optimization performance function, its constraints and the optimization engine configuration) along with its dependencies and traceability information. It is typically available to several collaborators. It is revisionned or versionned and has relations to other PLM objects. It has attributes such as directed parameters, performance function, exploration engine configuration, ... */
        in rawCompleteSimulationResult : RawCompleteSimulationResult;
        part toolConfiguration : ToolConfiguration;
        part simulationApplication : SimulationApplication;
        analysis optimizationConfiguration : OptimizationConfiguration;
        action optimizationAlgorithm : OptimizationAlgorithmDefinition;
    }

    analysis def OptimizationConfiguration :> TradeStudies::TradeStudy {
        ref part mdaoStrategyDefinition : MDAOStrategyDefinition;
        ref part mdaoProblemDefinition : MDAOProblemDefinition;
    }

    part def SimulationObject { 
        doc /* PLM container which hosts a simulation model (part of the behaviour models category) along with its dependencies and traceability information. It is typically available to several collaborators. It is revisionned or versionned and has relations to other PLM objects. It has attributes such as parameters. */
        ref part simulationModel [1] : SimulationModel;
        ref part systemDescription [1] : SystemDescription;
        attribute spdmAttributes[*] : Parameter;
        allocation connectedParameters [*] : ConnectedParameters allocate spdmAttributes to simulationModel.mbdExchangeAttributes;
    }

    enum def RequestType {
        CreateSimulationModel {

        }
        ModifySimulationModel {

        }
        CreateOptimization {

        }
        RunSimulation {

        }
        RunOptimization {

        }
    }

    part def ToolConfiguration {
        doc /* Set of rules and process definitions which the SPDM system uses to handle behavior models or simulation strategies. */
        attribute neededSimulationApplicationVersion;
    }

    part def SystemDescription {
        doc /* Model which describes a system from a logical, functional or physical point of view. This often includes a graphical representation which is a breakdown of the system into parts and how they interact between each other. */
    }

    part def SimulationPlatform {
        doc /* Multiple layers of hardware, OS and software able to host a simulation application. */
    }

    part def SimulationApplication :> ExecutableModelMetadata {
        doc /* Software which has the purpose of predicting the physical or logical behavior of a system by solving equations which often come from physical laws. A behavior model is a mandatory input of the simulation application. */
        attribute version : String;
        attribute toolName :>> authoringTool : String;
        attribute uri :>> externalModelLink : String;
    }

    enum def SimulationModelType {
        SystemModel {
            doc /* Model which describes a system from a logical, functional or physical point of view. This often includes a graphical representation which is a breakdown of the system into parts and how they interact between each other. */
        }
        BehaviorModel {
            doc /* Set of equations (or tables, response surface, or any other mean) approximating a physical or logical system’s response to stimuli.*/
        }
        CadModel {
            doc /* 3D representation of a physical part or assembly of physical parts. */
        }
        ProcessedCadModel :> CadModel {
            doc /* Designates a CAD which has been simplified or meshed for the purpose of being an input for a FEM or CFD simulation. */
        }
    }
  
    allocation def ConnectedParameters {
        doc /* Connects a parameter from the SPDM tool to a parameter or variable of a MBD tool. */
        end attribute spdmExchangeAttribute :>> source;
        end attribute mbdExchangeAttribute :>> target;
        allocate spdmExchangeAttribute to mbdExchangeAttribute;
    }

    part def SimulationModel :> MBDContainer {
        attribute modelPath : String default "modelPath";
        attribute simulationModelType : SimulationModelType;
        out rawCompleteSimulationResult : RawCompleteSimulationResult;
        attribute mbdExchangeAttributes = (exposedParameters, exposedVariables);
    }
}
