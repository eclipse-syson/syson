//
// Model-Based Design library
//
// Library for modeling model-based design concepts.
//
// This library has been developed as part of the ODE4HERA project: https://www.ode4hera.eu/
//
// Version: v2.0
// License: MIT
// Maintainer: DLR Institute of System Architectures in Aeronautics
// Contact: digital.engineering@dlr.de
// Topics: MBD, Model-Based Design
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

library package ModelBasedDesign {
    doc
    /*
     * Domain-specific library implementing Model-Based Design Capabilities:
     * - Meta-Information specific to MBD (for generic provenance meta-information see ODE4HERA library)
     * - Behavioral Model (linked but possibly different from System Model Architecture)
     * - Behavioral Model execution information (modelled as actions)
     * - Simulation information including execution and results
     *
     * Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
     */
    private import RequirementsManagement::ProvenanceAttributes;
    private import RequirementsManagement::Stakeholder;
    private import ArchitectureDesign::Architecture;
    private import SequenceFunctions::*;

    public import MBDSemanticMetadata::*;
    public import 'FMIv2.0.5'::*;

    // Meta-Information

    metadata def ExecutableModelMetadata specializes ProvenanceAttributes {  // shorten removing "Model", same for other metadata
        doc /*Container for meta-info related to executable models for MBD (and MDAO?)*/
        attribute authoringTool : ScalarValues::String [0..1] {
            doc /*Tool used to create the executable model,
            this is relevant since it might give constraints
            for the integration of the model in the execution platform*/
        }
        attribute domain : ScalarValues::String [0..1] {
            doc /*Engineering domain associated with the executable model.
            Example of engineering domains are Aerodynamic, Structure,
            Mechatronics, Thermodynamic, etc.*/
        }
        attribute externalModelLink : ScalarValues::String [0..1] {  // add details on type of link:library path, directory path: any std/ext lib support?
            doc /*Path to the actual executable model. Note
            that this is not necessarily an executable file, it could be an FMU or
            a specific file format to be run on a specific execution platform*/
        }
        part executor : Executor {  // make sense only for simulation architecture, not for simulation components 
            doc /*Engineer in charge of model execution (might be different from the
            engineer responsible for execution of entire/master workflow which is composed
            by several executable models)*/
        }
        :> annotatedElement: SysML::PartDefinition;
        :> annotatedElement: SysML::PartUsage;
        :> annotatedElement: SysML::PortDefinition;
        :> annotatedElement: SysML::PortUsage;
    }
    part def Executor :> Stakeholder {
        :>> risk[0];
        :>> effort[0];
    }

    metadata def BehavioralModelMetadata :> ExecutableModelMetadata {
        doc /*Container for meta-info specific to the behavioral model*/
        attribute timeBehavior:TimeBehavior;
        attribute dynamicType:DynamicType;
        attribute linearity:Linearity;
        attribute nonLinearityType:NonLinearityType;
        attribute timeDomainSolver:TimeDomainSolver; // used only for embedded time domain
        attribute embeddedTimeDomainSolver : ScalarValues::Boolean; //useful only for FMU?
        attribute maxTimeStep : ScalarValues::Real;
        attribute minTimeStep : ScalarValues::Real;
        :> annotatedElement: SysML::ActionDefinition;
        :> annotatedElement: SysML::ActionUsage;
    }
    enum def TimeBehavior {Static; Dynamic;}
    enum def DynamicType {TimeVarying; TimeInvariant;}
    enum def Linearity {Linear; NonLinear; }
    enum def NonLinearityType {ContinuousState; DiscreteState;}
    enum def TimeDomainSolver {
        comment /*Inspired by FMI, to be checked if the info should be exposed here*/
    }

    item def ExecutionConfiguration { // proposal: change name to SimulationConfiguration to clearly distinguish wrt ExecutableModelMetamodel
        doc /**/
        attribute simulationType : SimulationType;
        attribute verificationLogic : ExtractionLogic; // should be assign to each output since single simulation execution could verify multiple requirements
        attribute startTime : ScalarValues::Real;
        attribute stopTime : ScalarValues::Real;
        attribute numberOfIntervals : ScalarValues::Integer;
        attribute tolerance : ScalarValues::Real;
        attribute method : ScalarValues::String;
        attribute outputFormat : ScalarValues::String;
    }
    enum def SimulationType {Time; Frequency;}
    enum def ExtractionLogic {
        doc /* Represent rationale for extraction of (scalar) value from simulation results (often time series) */
        Max {doc /* Take maximum value */}
        Min {doc /* Take minimum value */}
        SteadyState {doc /* Take value before simulation ends */}
    }
    
    private import VectorValues::NumericalVectorValue;
    private import ControlFunctions::maximize;
    private import ControlFunctions::minimize;
    private import SequenceFunctions::last;

    attribute def ExtractedValue {
        doc /*Main container for value extracted from simulation result series*/
        attribute resultSeries: ScalarValues::ScalarValue;  // issue with kernel function definition should be VectorValues::NumericalVectorValues
        attribute value: ScalarValues::ScalarValue;  // issue with kernel function definition, should be ScalarValues::Real
    }
    attribute def maxValue :> ExtractedValue {
        doc /*Max value extracted from result series*/
        :>> value = maximize(resultSeries);
    }
    attribute def minValue :> ExtractedValue {
        doc /*Max value extracted from result series*/
        :>> value = minimize(resultSeries);
    }
    attribute def SteadyState :> ExtractedValue {
        doc /*Max value extracted from result series*/
        :>> value = last(resultSeries);
    }

    // Simulation elements

    abstract part def MBDContainer {
        doc /*MBD container decomposition (reusable abstract base definition for MBD modular unit of structure)*/

        metadata executableMetadata: ExecutableModelMetadata;

        abstract part components: SimulationComponent[0..*] :> subparts;
        abstract ref action behaviors: MBDBehavior[0..*] :> performedActions;

        attribute exposedParameters: MBDParameter[0..*] :> mbdParameters {
            doc /*Exposed parameters (a data value that is constant during the simulation and is provided by the environment and cannot be used in connections)*/
        }
        attribute exposedVariables: MBDVariable[0..*] :> mbdVariables {
            doc /*Exposed variables that are calculated from other variables or are continuous-time states.*/
        }

        item assets: MBDAsset[0..*] {
            doc /* Reference to a resource or an artifact that is created, used, or managed during the development process to drive behavioral modeling. */
        }
    }

    part def SimulationArchitecture :> MBDContainer {
        doc /*MBD architecture top-level model referencing and refining a corresponding system architecture model(s)*/
        ref part systemArchitecture: Architecture[1..*];
    }

    part def SimulationComponent :> MBDContainer {
        doc /*MBD simulation component (reusable modular unit of structure) referencing and refining a corresponding system architecture component(s)*/
        ref part systemArchitectureComponent[1..*];
    }

    action def MBDBehavior {
        doc /*Base definition of executable behavioral model (e.g. an equation, function, or executable code) referencing a context of a corresponding system architecture behavior*/
        ref part systemArchitectureComponent[0..*];

        // metadata behavioralModelMetadata: BehavioralModelMetadata;

        protected attribute parameters: MBDParameter[0..*] :> mbdParameters {
            doc /*Independent parameters (a data value that is constant during the simulation and is provided by the environment and cannot be used in connections)*/
        }
        protected attribute variables: MBDVariable[0..*] :> mbdVariables {
            doc /*Local variable that is calculated from other variables or is a continuous-time state. It is not allowed to use the variable value in another model.*/
        }
    }

    // Currently it is not really possible to create a new attribute def,
    // because then literal values will not inherit from this attribute def and an incompatible data error will be thrown by Syside

    // attribute def MBDParameter;
    alias MBDParameter for Base::DataValue;
    attribute mbdParameters : MBDParameter[*];
    // attribute def MBDVariable;
    alias MBDVariable for Base::DataValue;
    attribute mbdVariables: MBDVariable[*];

    item def MBDAsset {
        doc /*MBD asset refers to any resource or artifact that is created, used, or managed during the development process to support behavioral modeling.
            These assets are essential for designing, implementing, verifying, and documenting the system. */
        attribute type: MBDAssetType[1] default MBDAssetType::Document;
        attribute uri: ScalarValues::String[0..1];
    }

    enum def MBDAssetType {
        Document;
        Specification;
        UseCaseSpecification;
        RequirementsSpecification;
        DesignSpecification;
        ConstraintsSpecification;
        SystemModel;
        BehavioralModel;
        CADModel;
        ExecutableCode;
        TestCase;
        Documentation;
        Diagram;
        SimulationData;
    }

    // Simulation 

    item def AnalysisScenario {
        doc /**/
    }
    item def SimulationResults {
        doc /**/
    }
    analysis def SimulationRun {
        doc /**/
        subject mbdSubject: MBDContainer;
        in configuration: ExecutionConfiguration;
        in scenario: AnalysisScenario;

        return results: SimulationResults;
    }

    // External links

    action def FMUBehavior :> MBDBehavior {
        doc /*Specialization of general MBD behavior in terms of FMI standard*/
        item fmiDescription: 'FMIv2.0.5'::fmiModelDescription;
    }
    abstract ref action fmuBehaviors: FMUBehavior[*];

    package 'FMIv2.0.5' {
        doc /*Functional Mock-up Interface for Model Exchange and Co-Simulation v2.0.5 (https://fmi-standard.org/)*/
        item def fmiModelDescription :> SysML::Element {
            doc /*Static information related to an FMU and stored in the text file modelDescription.xml in XML format */
            attribute fmiVersion: ScalarValues::String[1] = "2.0" {
                doc /*Version of "FMI for Model Exchange or Co-Simulation" that was used
                to generate the XML file. The value for this version is "2.0".*/
            }
            attribute modelName: ScalarValues::String[1] {
                doc /*The name of the model as used in the modeling environment that generated the XML file, such as e.g.
                "Modelica.Mechanics.Rotational.Examples.CoupledClutches".*/
            }
            alias guid for elementId {
                doc /*The "Globally Unique IDentifier" is a string that is used to check that
                the XML file is compatible with the C functions of the FMU. Typically,
                when generating the XML file, a fingerprint of the “relevant” information
                is stored as guid and in the generated C-function. */
            }
            attribute description: ScalarValues::String[0..1] {doc /*Optional string with a brief description of the model.*/}
            attribute author: ScalarValues::String[0..1] {doc /*Optional string with the name and organization of the model author.*/}
            attribute version: ScalarValues::String[0..1] {doc /*Optional version of the model, for example “1.0”.*/}
            attribute copyright: ScalarValues::String[0..1] {doc /*Optional information on the intellectual property copyright for this FMU.*/}
            attribute license: ScalarValues::String[0..1] {doc /*Optional information on the intellectual property licensing for this FMU.*/}
            attribute generationTool: ScalarValues::String[0..1] {doc /*Optional name of the tool that generated the XML file.*/}
            attribute generationDateAndTime: Time::DateTime[0..1] {doc /*Optional date and time when the XML file was generated.*/}
            attribute variableNamingConvention: VariableNamingConvention[0..1] default VariableNamingConvention::flat {
                doc /*Defines whether the variable names in "ModelVariables / ScalarVariable / name" and in
                "TypeDefinitions / Type / name" follow a particular convention.*/
            }
            attribute numberOfEventIndicators: ScalarValues::Integer[0..1] {
                doc /*The (fixed) number of event indicators for an FMU based on FMI for Model Exchange.
                For Co-Simulation, this value is ignored.*/
                assert constraint { that >= 0 }
            }
            attribute fmuType: FMUType[1];
            item modelExchange: FMUTypeModelExchange[0..1];
            assert constraint fmuTypeModelExchangeConstraint {
                (fmuType == FMUType::ModelExchange or fmuType == FMUType::ModelExchangeAndCoSimulation) implies SequenceFunctions::size(modelExchange) == 1
            }
            item coSimulation: FMUTypeCoSimulation[0..1];
            assert constraint fmuTypeCoSimulationConstraint {
                (fmuType == FMUType::CoSimulation or fmuType == FMUType::ModelExchangeAndCoSimulation) implies SequenceFunctions::size(coSimulation) == 1
            }
            item unitDefinitions: FMUUnitDefinitions[0..1];
            item typeDefinitions: FMUTypeDefinitions[0..1];
            item logCategories: FMULogCategories[0..1];
            item defaultExperiment: FMUDefaultExperiment[0..1];
            item vendorAnnotations: FMUVendorAnnotations[0..1];
            item modelVariables: FMUModelVariables[1];
            item modelStructure: FMUModelStructure[1];
        }
        enum def VariableNamingConvention :> ScalarValues::String {flat = "flat"; structured = "structured";}
        enum def FMUType {ModelExchange; CoSimulation; ModelExchangeAndCoSimulation;}

        item def FMUTypeModelExchange {
            attribute modelIdentifier: ScalarValues::String[1];
            attribute needsExecutionTool: ScalarValues::Boolean[0..1] default false;
            attribute completedIntegratorStepNotNeeded: ScalarValues::Boolean[0..1] default false;
            attribute canBeInstantiatedOnlyOncePerProcess: ScalarValues::Boolean[0..1] default false;
            attribute canNotUseMemoryManagementFunctions: ScalarValues::Boolean[0..1] default false;
            attribute canGetAndSetFMUstate: ScalarValues::Boolean[0..1] default false;
            attribute canSerializeFMUstate: ScalarValues::Boolean[0..1] default false;
            attribute providesDirectionalDerivative: ScalarValues::Boolean[0..1] default false;
            item sourceFiles: FMUSourceFile[0..*];
        }

        item def FMUTypeCoSimulation {
            attribute modelIdentifier: ScalarValues::String[1];
            attribute needsExecutionTool: ScalarValues::Boolean[0..1] default false;
            attribute canHandleVariableCommunicationStepSize: ScalarValues::Boolean[0..1] default false;
            attribute canInterpolateInputs: ScalarValues::Boolean[0..1] default false;
            attribute maxOutputDerivativeOrder: ScalarValues::Integer[0..1] default 0 { assert constraint { that >= 0 } }
            attribute canRunAsynchronuously: ScalarValues::Boolean[0..1] default false;
            attribute canBeInstantiatedOnlyOncePerProcess: ScalarValues::Boolean[0..1] default false;
            attribute canNotUseMemoryManagementFunctions: ScalarValues::Boolean[0..1] default false;
            attribute canGetAndSetFMUstate: ScalarValues::Boolean[0..1] default false;
            attribute canSerializeFMUstate: ScalarValues::Boolean[0..1] default false;
            attribute providesDirectionalDerivative: ScalarValues::Boolean[0..1] default false;
            item sourceFiles: FMUSourceFile[0..*];
        }
        item def FMUSourceFile {
            attribute name: ScalarValues::String[1];
        }

        item def FMUUnitDefinitions {
            item units: fmi2Unit[1..*];
        }
        item def fmi2Unit {
            attribute name: ScalarValues::String[1];
            item baseUnit: fmi2UnitBaseUnit[0..1];
            item displayUnit: fmi2UnitDisplayUnit[0..*];
        }
        item def fmi2UnitBaseUnit {
            doc /*BaseUnit_value = factor * Unit_value + offset*/
            attribute kg: ScalarValues::Integer[0..1] default 0 {doc /*Exponent of SI base unit "kg"*/}
            attribute m: ScalarValues::Integer[0..1] default 0 {doc /*Exponent of SI base unit "m"*/}
            attribute s: ScalarValues::Integer[0..1] default 0 {doc /*Exponent of SI base unit "s"*/}
            attribute A: ScalarValues::Integer[0..1] default 0 {doc /*Exponent of SI base unit "A"*/}
            attribute K: ScalarValues::Integer[0..1] default 0 {doc /*Exponent of SI base unit "K"*/}
            attribute mol: ScalarValues::Integer[0..1] default 0 {doc /*Exponent of SI base unit "mol"*/}
            attribute cd: ScalarValues::Integer[0..1] default 0 {doc /*Exponent of SI base unit "cd"*/}
            attribute rad: ScalarValues::Integer[0..1] default 0 {doc /*Exponent of SI derived unit "rad"*/}
            attribute factor: ScalarValues::Real[0..1] default 1.0;
            attribute offset: ScalarValues::Real[0..1] default 0.0;
        }
        item def fmi2UnitDisplayUnit {
            doc /*DisplayUnit_value = factor * Unit_value + offset*/
            attribute name: ScalarValues::String[1];
            attribute factor: ScalarValues::Real[0..1] default 1.0;
            attribute offset: ScalarValues::Real[0..1] default 0.0;
        }

        item def FMUTypeDefinitions {
            item simpleTypes: fmi2SimpleType[1..*];
        }
        abstract item def fmi2SimpleType {
            attribute name: ScalarValues::String[1];
            attribute description: ScalarValues::String[0..1];
        }
        item def fmi2SimpleTypeReal :> fmi2SimpleType, fmi2RealAttributes;
        item def fmi2SimpleTypeInteger :> fmi2SimpleType, fmi2IntegerAttributes;
        item def fmi2SimpleTypeEnumeration :> fmi2SimpleType {
            attribute quantity: ScalarValues::String[0..1];
            item items: fmi2SimpleTypeEnumerationItem[1..*];
        }
        item def fmi2SimpleTypeEnumerationItem {
            attribute name: ScalarValues::String[1];
            attribute value: ScalarValues::Integer[1];
            attribute description: ScalarValues::String[0..1];
        }
        item def fmi2RealAttributes {
            attribute quantity: ScalarValues::String[0..1];
            attribute unit: ScalarValues::String[0..1];
            attribute displayUnit: ScalarValues::String[0..1];
            attribute relativeQuantity: ScalarValues::Boolean[0..1] default false;
            attribute min: ScalarValues::Real[0..1];
            attribute max: ScalarValues::Real[0..1];
            assert constraint { max >= min }
            attribute nominal: ScalarValues::Real[0..1];
            assert constraint { nominal > 0.0 }
            attribute unbounded: ScalarValues::Boolean[0..1] default false;
        }
        item def fmi2IntegerAttributes {
            attribute quantity: ScalarValues::String[0..1];
            attribute min: ScalarValues::Integer[0..1];
            attribute max: ScalarValues::Integer[0..1];
            assert constraint { max >= min }
        }

        item def FMULogCategories {
            doc /*Log categories available in FMU*/
            item categories: fmi2Category[1..*];
        }
        enum def FMUStandardizedCategoryName :> ScalarValues::String {
            logEvents = "logEvents" {doc /*Log all events (during initialization and simulation).*/}
            logSingularLinearSystems = "logSingularLinearSystems" {
                doc /*Log the solution of linear systems of equations if the solution is 
                singular (and the tool picked one solution of the infinitely many solutions).*/
            }
            logNonlinearSystems = "logNonlinearSystems" {doc /*Log the solution of nonlinear systems of equations. */}
            logDynamicStateSelection = "logDynamicStateSelection" {doc /*Log the dynamic selection of states. */}
            logStatusWarning = "logStatusWarning" {doc /*Log messages when returning fmi2Warning status from any function.*/}
            logStatusDiscard = "logStatusDiscard" {doc /*Log messages when returning fmi2Discard status from any function.*/}
            logStatusError = "logStatusError" {doc /*Log messages when returning fmi2Error status from any function.*/}
            logStatusFatal = "logStatusFatal" {doc /*Log messages when returning fmi2Fatal status from any function.*/}
            logStatusPending = "logStatusPending" {doc /*Log messages when returning fmi2Pending status from any function.*/}
            logAll = "logAll" {doc /*Log all messages.*/}
        }
        item def fmi2Category {
            attribute name: ScalarValues::String, FMUStandardizedCategoryName[1] {doc /*the value can be arbitrary or the one of the standardized category names defined by FMUStandardizedCategoryName*/}
            attribute description: ScalarValues::String[0..1] {doc /*Description of the log category*/}
        }

        item def FMUDefaultExperiment {
            doc /*DefaultExperiment consists of the optional default start time, stop time, relative tolerance, and step size 
            for the first simulation run. A tool may ignore this information. However, it is convenient for a user that 
            startTime, stopTime, tolerance and stepSize have already a meaningful default value for the model at hand.*/
    
            attribute startTime: ScalarValues::Real[0..1] {doc /*Default start time of simulation*/}
            attribute stopTime: ScalarValues::Real[0..1] {doc /*Default stop time of simulation*/}
            attribute tolerance: ScalarValues::Real[0..1] {doc /*Default relative integration tolerance*/}
            attribute stepSize: ScalarValues::Real[0..1] {doc /*ModelExchange: Default step size for fixed step integrators, CoSimulation: Preferred communicationStepSize*/}
        }

        item def FMUVendorAnnotations {
            item annotations: fmi2Annotation[1..*];
        }
        item def fmi2Annotation {
            doc /*Tool specific data (ignored by other tools)*/
            item tool: FMUVendorAnnotationTool[1];
        }
        item def FMUVendorAnnotationTool {
            attribute name: ScalarValues::String[1] {doc /*Name of tool that can interpret the annotation*/}
            item any: Base::Anything[0..1] {doc /*Can be an arbitrary (XML) data structure defined by the tool*/}
        }

        item def FMUModelVariables {
            doc /*The central FMU data structure defining all variables of the FMU that are visible/accessible
            via the FMU functions. Ordered list of all variables (first definition has index = 1).*/
            item variables: fmi2ScalarVariable[1..*] ordered;
        }
        item def fmi2ScalarVariable {
            attribute name: ScalarValues::String[1] {doc /*Identifier of a variable (must be unique with respect to all other elements of the ModelVariables list)*/}
            attribute valueReference: ScalarValues::Integer[1] {
                doc /*Identifier for variable value in FMI2 function calls (not necessarily unique with respect to all variables)*/
                assert constraint { that >= 0 }
            }
            attribute description: ScalarValues::String[0..1];
            attribute causality: FMUVariableCausality[0..1] default FMUVariableCausality::local {doc /*Enumeration that defines the causality of the variable.*/}
            attribute variability: FMUVariableVariability[0..1] default FMUVariableVariability::continuous {
                doc /* Enumeration that defines the time dependency of the variable, in other words, it 
                defines the time instants when a variable can change its value*/
            }
            attribute initial: FMUVariableInitialization[0..1] {doc /*If not provided, inital is deduced from causaility and variability (details see specification)*/}
            attribute canHandleMultipleSetPerTimeInstant: ScalarValues::Boolean[0..1] {
                doc /*Only for ModelExchange and only for variables with variability = "input":
                If present with value = false, then only one fmi2SetXXX call is allowed at one super dense time instant.
                In other words, this input is not allowed to appear in an algebraic loop.*/
            }
            item type: fmi2ScalarVariableType[1];
            item vendorAnnotations: FMUVendorAnnotations[0..1] {doc /*Additional data of the scalar variable, e.g., for the dialog menu or the graphical layout*/}
            assert constraint parameterCausalityConstraint {
                (causality == FMUVariableCausality::parameter or causality == FMUVariableCausality::calculatedParameter) implies
                    (variability == FMUVariableVariability::fixed or variability == FMUVariableVariability::tunable)
            }
            assert constraint inputCausalityConstraint {
                causality == FMUVariableCausality::input implies
                    (
                        (variability == FMUVariableVariability::discrete or variability == FMUVariableVariability::continuous) and
                        SequenceFunctions::size(initial) == 0 and
                        SequenceFunctions::size(type.startValue) == 1
                    )
            }
            assert constraint outputCausalityConstraint {
                causality == FMUVariableCausality::output implies
                    (variability == FMUVariableVariability::'constant' or variability == FMUVariableVariability::discrete or variability == FMUVariableVariability::continuous)
            }
            assert constraint independentCausalityConstraint {
                causality == FMUVariableCausality::independent implies
                    (variability == FMUVariableVariability::continuous and SequenceFunctions::size(initial) == 0 and SequenceFunctions::size(type.startValue) == 0)
            }
            assert constraint initializationConstraint {
                SequenceFunctions::size(initial) == 1 implies (
                    (
                        (causality == FMUVariableCausality::parameter and (variability == FMUVariableVariability::fixed or variability == FMUVariableVariability::tunable)) implies 
                            initial == FMUVariableInitialization::exact
                    ) and
                    (
                        ((causality == FMUVariableCausality::output or causality == FMUVariableCausality::local) and variability == FMUVariableVariability::'constant') implies 
                            initial == FMUVariableInitialization::exact
                    ) and
                    (
                        ((causality == FMUVariableCausality::calculatedParameter or causality == FMUVariableCausality::local) and 
                        (variability == FMUVariableVariability::fixed or variability == FMUVariableVariability::tunable)) implies 
                            (initial == FMUVariableInitialization::approx or initial == FMUVariableInitialization::calculated)
                    ) and
                    (
                        ((causality == FMUVariableCausality::output or causality == FMUVariableCausality::local) and 
                        (variability == FMUVariableVariability::discrete or variability == FMUVariableVariability::continuous)) implies 
                            (initial == FMUVariableInitialization::exact or initial == FMUVariableInitialization::approx or initial == FMUVariableInitialization::calculated)
                    )
                )
            }
        }
        enum def FMUVariableCausality :> ScalarValues::String {
            parameter = "parameter" {doc /*independent parameter*/}
            calculatedParameter = "calculatedParameter" {doc /*calculated parameter*/}
            input = "input" {doc /*can be used in connections*/}
            output = "output" {doc /*can be used in connections*/}
            local = "local" {doc /*variable calculated from other variables*/}
            independent = "independent" {doc /*independent variable (usually time)*/}
        }
        enum def FMUVariableVariability :> ScalarValues::String {
            'constant' = "constant" {doc /*value never changes*/}
            fixed = "fixed" {doc /*value fixed after initialization*/}
            tunable = "tunable" {doc /*value constant between external events*/}
            discrete = "discrete" {doc /*value constant between internal events*/}
            continuous = "continuous" {doc /*no restriction on value changes*/}
        }
        enum def FMUVariableInitialization :> ScalarValues::String {
            exact = "exact" {doc /*initialized with start value*/}
            approx = "approx" {doc /*iteration variable that starts with start value*/}
            calculated = "calculated" {doc /*calculated from other variables*/}
        }
        abstract item def fmi2ScalarVariableType {
            attribute declaredType: ScalarValues::String[0..1] {doc /*If present, name of type defined with TypeDefinitions / SimpleType providing defaults.*/}
            attribute startValue: ScalarValues::ScalarValue[0..1] {doc /*Value before initialization, if initial=exact or approx*/}
        }
        item def fmi2ScalarVariableTypeReal :> fmi2ScalarVariableType, fmi2RealAttributes {
            :>> startValue: ScalarValues::Real {
                doc /*Value before initialization, if initial=exact or approx, max >= startValue >= min required*/
                assert constraint { startValue >= min and startValue <= max }
            }
            attribute derivative: ScalarValues::Integer[0..1] {
                doc /*If present, this variable is the derivative of variable whith ScalarVariable index "derivative"*/
            }
            attribute reinit: ScalarValues::Boolean[0..1] {
                doc /*Only for ModelExchange and if variable is a continuous-time state:
                If true, state can be reinitialized at an event by the FMU;
                If false, state will never be reinitialized at an event by the FMU.*/
            }
        }
        item def fmi2ScalarVariableTypeInteger :> fmi2ScalarVariableType, fmi2IntegerAttributes {
            :>> startValue: ScalarValues::Integer {
                doc /*Value before initialization, if initial=exact or approx, max >= startValue >= min required*/
                assert constraint { startValue >= min and startValue <= max }
            }
        }
        item def fmi2ScalarVariableTypeBoolean :> fmi2ScalarVariableType {
            :>> startValue: ScalarValues::Boolean;
        }
        item def fmi2ScalarVariableTypeString :> fmi2ScalarVariableType {
            :>> startValue: ScalarValues::String;
        }
        item def fmi2ScalarVariableTypeEnumeration :> fmi2ScalarVariableType {
            :>> declaredType[1] {doc /*Name of type defined with TypeDefinitions / SimpleType*/}
            attribute quantity: ScalarValues::String[0..1];
            attribute min: ScalarValues::Integer[0..1];
            attribute max: ScalarValues::Integer[0..1];
            :>> startValue: ScalarValues::Integer;
        }

        item def FMUModelStructure {
            doc /*Ordered lists of outputs, exposed state derivatives, and the initial unknowns. Optionally, the functional dependency of these variables can be defined.*/
            item outputs: FMUModelStructureVariableDependencies[0..1] {
                doc /*Ordered list of all outputs, in other words a list of ScalarVariable indices
                where every corresponding ScalarVariable must have causality="output" (and every
                variable with causality="output" must be listed here).*/
            }
            item derivatives: FMUModelStructureVariableDependencies[0..1] {
                doc /*Ordered list of all state derivatives, in other words, a list of ScalarVariable
                indices where every corresponding ScalarVariable must be a state derivative.*/
            }
            item initialUnknows: FMUModelStructureInitialUnknowns[0..1] {
                doc /*Ordered list of all exposed Unknowns in Initialization Mode.*/
            }
        }
        item def FMUModelStructureVariableDependencies {
            item unknowns: fmi2VariableDependencyUnknown[1..*] ordered;
        }
        item def fmi2VariableDependencyUnknown {
            doc /*Dependency of scalar Unknown from Knowns in Continuous-Time and Event Mode (ModelExchange),
            and at Communication Points (CoSimulation): Unknown = f(Known_1, Known_2, ...). The Knowns are
            "inputs", "continuous states", "independent variables" (usually time)*/
            attribute index: ScalarValues::Integer[1] {
                doc /*ScalarVariable index of Unknown*/
                assert constraint { that > 0 }
            }
            attribute dependencies: Collections::OrderedCollection[0..1] {
                doc /*Defines the dependency of the Unknown (directly or indirectly via auxiliary variables)
                on the Knowns in Continuous-Time and Event Mode (ModelExchange) and at Communication Points
                (CoSimulation). If not present, it must be assumed that the Unknown depends on all Knowns.
                If present as empty list, the Unknown depends on none of the Knowns. Otherwise the Unknown
                depends on the Knowns defined by the given ScalarVariable indices. The indices are ordered
                according to size, starting with smallest index.*/
                :>> elements: ScalarValues::Integer;
            }
            attribute dependenciesKind: Collections::OrderedCollection[0..1] {
                doc /*If not present, it must be assumed that the Unknown depends on the Knowns without a
                particular structure. Otherwise, the corresponding Known v enters the equation as:
                = "dependent": no particular structure, f(v),
                = "constant": constant factor c*v (only for Real variables),
                = "fixed": fixed factor p*v (only for Real variables),
                = "tunable": tunable factor p*v (only for Real variables),
                = "discrete": discrete factor d*v (only for Real variables).
                If "dependenciesKind" is present, "dependencies" must be present and must have the same
                number of list elements.*/
                :>> elements: fmi2VariableDependencyKind;
            }
            assert constraint {CollectionFunctions::size(dependencies) == CollectionFunctions::size(dependenciesKind)}
        }
        enum def fmi2VariableDependencyKind :> ScalarValues::String {
            dependent = "dependent";
            'constant' = "constant";
            fixed = "fixed";
            tunable = "tunable";
            discrete = "discrete";
        }
        item def FMUModelStructureInitialUnknowns :> FMUModelStructureVariableDependencies {
            :>> unknowns: fmi2VariableDependencyInitialUnknown;
        }
        item def fmi2VariableDependencyInitialUnknown :> fmi2VariableDependencyUnknown {
            doc /*Dependency of scalar Unknown from Knowns: Unknown = f(Known_1, Known_2, ...). The Knowns are
            "inputs", "variables with initial=exact", and "independent variable".*/
            :>> dependencies {
                doc /*Defines the dependency of the Unknown (directly or indirectly via auxiliary variables)
                on the Knowns in the Initialization Mode. If not present, it must be assumed that the Unknown
                depends on all Knowns. If present as empty list, the Unknown depends on none of the Knowns.
                Otherwise the Unknown depends on the Knowns defined by the given ScalarVariable indices.
                The indices are ordered according to size, starting with smallest index.*/
            }
            :>> dependenciesKind {
                doc /*If not present, it must be assumed that the Unknown depends on the Knowns without a
                particular structure. Otherwise, the corresponding Known v enters the equation as:
                = "dependent": no particular structure, f(v),
                = "constant": constant factor c*v (only for Real variables).
                If "dependenciesKind" is present, "dependencies" must be present and must have the same
                number of list elements.*/
                :>> elements: fmi2InitialUnknownDependencyKind;
            }
        }
        enum def fmi2InitialUnknownDependencyKind :> ScalarValues::String {
            dependent = "dependent";
            'constant' = "constant";
        }
    }

    // Semantic metadata

    package MBDSemanticMetadata {
        doc /*Domain-specific Semantic Metadata definitions (DSM)*/

        part simulationArchitectures: SimulationArchitecture[*];
        metadata def <mbd_architecture> SimulationArchitectureDSM :> Metaobjects::SemanticMetadata {
            doc /*#mbd_architecture declares a simulation architecture model*/
            :> annotatedElement: SysML::PartUsage;
            :>> baseType = simulationArchitectures meta SysML::PartUsage;
        }
        metadata def <mbd_component> SimulationComponentDSM :> Metaobjects::SemanticMetadata {
            doc /*#mbd_component declares a simulation component model*/
            :> annotatedElement: SysML::PartDefinition;
            :> annotatedElement: SysML::PartUsage;
            :>> baseType = MBDContainer::components meta SysML::PartUsage;
        }
        metadata def <mbd_behavior> SimulationBehaviorDSM :> Metaobjects::SemanticMetadata {
            doc /*#mbd_behavior declares a simulation behavior (a function, equation, or executable code)*/
            :> annotatedElement: SysML::ActionUsage;
            :>> baseType = MBDContainer::behaviors meta SysML::ActionUsage;
        }
        metadata def <fmu_behavior> FMUBehaviorDSM :> Metaobjects::SemanticMetadata {
            doc /*#fmu_behavior declares a FMU behavior (FMI metadata)*/
            :> annotatedElement: SysML::ActionUsage;
            :>> baseType = fmuBehaviors meta SysML::ActionUsage;
        }
        metadata def <mbd_parameter> MBDParameterDSM :> Metaobjects::SemanticMetadata {
            doc /*#mbd_parameter declares independent parameters (a data value that is constant during the simulation and is provided by the environment and cannot be used in connections)*/
            :> annotatedElement: SysML::AttributeUsage;
            :>> baseType = mbdParameters meta SysML::AttributeUsage;
        }
        metadata def <mbd_variable> MBDVariableDSM :> Metaobjects::SemanticMetadata {
            doc /*#mbd_variable declares local variables*/
            :> annotatedElement: SysML::AttributeUsage;
            :>> baseType = mbdVariables meta SysML::AttributeUsage;
        }
        metadata def <mbd_exposed_parameter> MBDExposedParameterDSM :> Metaobjects::SemanticMetadata {
            doc /*#mbd_exposed_parameter declares exposed parameters in structural model elements*/
            :> annotatedElement: SysML::AttributeUsage;
            :>> baseType = MBDContainer::exposedParameters meta SysML::AttributeUsage;
        }
        metadata def <mbd_exposed_variable> MBDExposedVariableDSM :> Metaobjects::SemanticMetadata {
            doc /*#mbd_exposed_variable declares exposed variables in structural model elements*/
            :> annotatedElement: SysML::AttributeUsage;
            :>> baseType = MBDContainer::exposedVariables meta SysML::AttributeUsage;
        }
        metadata def <mbd_asset> MBDAssetDSM :> Metaobjects::SemanticMetadata {
            doc /*#mbd_asset declares MBD asset references in structural model elements*/
            :> annotatedElement: SysML::ItemUsage;
            :>> baseType = MBDContainer::assets meta SysML::ItemUsage;
        }
    }

}