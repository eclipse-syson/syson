/*
 * Copyright (c) 2025 OBEO, THALES GLOBAL SERVICES.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial implementation
 */

library package Arcadia {
    private import ScalarValues::Boolean;
    private import ScalarValues::String;
    private import Flows::MessageAction;
	private import KerML::FeatureDirectionKind; 
	private import KerML::Feature;
	private import ModelingMetadata::*;
		
	abstract occurrence def ArcadiaElement{
    	private doc /* Reusable structure shared by all Arcadia elements.*/ 
        attribute description : String;
        @StatusInfo {status = StatusKind::tbd;}
        // We do not include the Capella field "summary" as we consider it is rarely used
        // We do not include the Capella field "title" as we will use the default names of objects
    }

    metadata def ArcadiaPackage {
		private doc /* Used to annotate package elements in an Arcadia model */
		:> annotatedElement : SysML::Package;
		enum engineeringPerspective : ArcadiaEngineeringPerspective;
     }
	
	enum def ArcadiaEngineeringPerspective :> String { 
    	doc /* Flag to indicate which engineering perspective you're in.*/
       	enum OperationalAnalysis = "Operational Analysis";
       	enum SystemAnalysis = "System Analysis";
       	enum LogicalArchitecture = "Logical Architecture";
       	enum PhysicalArchitecture = "Physical Architecture";
       	enum EPBS = "EPBS Physical Architecture";
      	enum Generic = "Generic";
       	enum SystemEngineering = "System Engineering";
  	}

	part def Component :> ArcadiaElement {
		private doc /* A generic Arcadia Component, used across all Arcadia engineering perspectives. */
    	attribute isActor : Boolean default false;
    	attribute isHuman : Boolean default false;
	    // We do not create a specific "Allocation" relationship between Components and Functions but rather reuse the "perform action" relationship
		derived ref allocatedFunctions : Function[*] = performedActions; // Derived list of allocated Arcadia Functions from performed actions
		derived ref subComponents : Component[*] = subparts; // Represents the decomposition of an Arcadia Component into subcomponents via subparts
	}

	action def Function :> ArcadiaElement {
	    private doc /* A  generic Arcadia Function, used across all Arcadia engineering perspectives. */
        derived ref subFunctions: Function[*] = subactions; // Represents the functional decomposition of an Arcadia Function
        // We map function ports to standard in/out/inout parameters of actions (using ExchangeItems)
    }
    
    action def FunctionalChain :> ArcadiaElement  {
        private doc /*  A  generic Arcadia Functional Chain, used across all Arcadia engineering perspectives. */
        ref involvedFunctionalExchanges: FunctionalExchange[*];
        derived ref involvedFunctions : Function[*] = (involvedFunctionalExchanges.source, involvedFunctionalExchanges.target);
    }

	flow def FunctionalExchange :> ArcadiaElement{
	    private doc /* A  generic Arcadia Functional Exchange, used across all Arcadia engineering perspectives. */
        // We need to check if we can have more than one Exchange Item
		ref item :>> payload : ExchangeItem;
		end source : Function;
		end target : Function;
	}

    allocation def ExchangeAllocation :> ArcadiaElement {
	    private doc /* A  generic Arcadia Exchange Allocation, used across all Arcadia engineering perspectives. */
        end functionalExchange : MessageAction;
        end componentExchange : ComponentExchange;
    }
    
	item def ExchangeItem :> ArcadiaElement{
		private doc /* A  generic Arcadia ExchangeItem, used across all Arcadia engineering perspectives. */
	}

    port def ComponentPort :> ArcadiaElement {
		private doc /* A  generic Arcadia Component Port, used across all Arcadia engineering perspectives. */
        attribute direction : FeatureDirectionKind;
		ref allocatedFunctionPorts: Feature[*];
    }

    interface def ComponentExchange :> ArcadiaElement {
    	private doc /* A  generic Component Exchange, used across all Arcadia engineering perspectives.*/
        end cp1 : ComponentPort;
        end cp2 : ComponentPort;
        ref allocatedExchangeItems: ExchangeItem[*];
        ref allocatedFunctionalExchanges: FunctionalExchange[*];
    }

    // No explicit ID, since the short name serves as the requirements ID in SysML v2
	requirement def ArcadiaRequirement :> ArcadiaElement{
    	private doc /* A  generic Requirement, used across all Arcadia engineering perspectives.*/		
	}
}