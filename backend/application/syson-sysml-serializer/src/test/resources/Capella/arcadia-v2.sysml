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
		
	abstract occurrence def ArcadiaElement {
    	private doc /* Reusable structure shared by all Arcadia elements.*/ 
		ref realizes : ArcadiaElement[*];
		ref isRealizedBy : ArcadiaElement[*];
    }

    metadata def ArcadiaPackage {
		private doc /* Used to annotate Packages elements in an Arcadia model */
		:> annotatedElement : SysML::PartDefinition;
		enum engineeringPerspective : ArcadiaEngineeringPerspective;
     }
	
	enum def ArcadiaEngineeringPerspective :> String { 
    	//TODO: Bug in pilot implementation, private doc should work
    	/*private*/ doc /* flag to indicate which engineering perspective you're in.*/
       	enum OperationalAnalysis = "Operational Analysis";
       	enum SystemAnalysis = "System Analysis";
       	enum LogicalArchitecture = "Logical Architecture";
       	enum PhysicalArchitecture = "Physical Architecture";
       	enum EPBS = "EPBS Physical Architecture";
      	enum Generic = "Generic";
       	enum SystemEngineering = "System Engineering";
  	}
 	
	part def Component :> ArcadiaElement {
		private doc /* A generic Arcadia component, used across all Arcadia engineering perspectives. */
    	attribute isActor : Boolean default false;
    	attribute isHuman : Boolean default false;
    	alias allocatedFunctions for performedActions;
		alias subComponents for subparts;
	}

    port def ComponentPort :> ArcadiaElement {
		private doc /* A  generic Arcadia Component Port, used across all Arcadia engineering perspectives. */
        attribute direction : FeatureDirectionKind;
		ref allocatedFunctionPorts: ExchangeItem [*];
    }

    interface def ComponentExchange :> ArcadiaElement {
    	private doc /* A  generic Component Exchange, used across all Arcadia engineering perspectives.*/
        end cp1 : ComponentPort;
        end cp2 : ComponentPort;
        ref allocatedExchangeItems: ExchangeItem[*];
        ref allocatedFunctionalExchanges: FunctionalExchange[*];
    }

	action def Function :> ArcadiaElement {
	    private doc /* A  generic Arcadia Function, used across all Arcadia engineering perspectives. */
         alias subFunctions for subactions;
    }
    
    action def FunctionalChain :> ArcadiaElement  {
        private doc /*  A  generic Arcadia Functional Chain, used across all Arcadia engineering perspectives. */
        ref involvedFunctionalExchanges: FunctionalExchange[*];
        //TODO Tim: Find out how to navigate from the Exchange Items (parameters) to the actual functions/actions
        derived ref involvedFunctions : Function[*] = (involvedFunctionalExchanges.source, involvedFunctionalExchanges.target);
    }

	flow def FunctionalExchange :> ArcadiaElement{
	    private doc /* A  generic Arcadia Functional Exchange, used across all Arcadia engineering perspectives. */
		ref :>> payload : ExchangeItem;
		//TODO Tim: Here I am trying to restrict the source and target of the flow to Exchange Items. But you said that Flow Defs points to Functions usages, and Flow Usage to the payload. So my writing is not correct here, right? How to do it?
		end source : ExchangeItem;
		end target : ExchangeItem;
	}

    allocation def ExchangeAllocation :> ArcadiaElement {
	    private doc /* A  generic Arcadia Exchange Allocation, used across all Arcadia engineering perspectives. */
        end functionalExchange : FunctionalExchange;
        end componentExchange : ComponentExchange;
    }
    
	item def ExchangeItem :> ArcadiaElement{
		private doc /* A  generic Arcadia ExchangeItem, used across all Arcadia engineering perspectives. */
	}

	requirement def ArcadiaRequirement :> ArcadiaElement{
    	private doc /* A  generic Requirement, used across all Arcadia engineering perspectives.*/		
	}
	
	occurrence def Capability :> ArcadiaElement {
	    private doc /* A  generic Arcadia Capability, used across all Arcadia engineering perspectives. */
		ref involvedFunctions: Function[*];
		ref involvedFunctionalChains: FunctionalChain[*];
		ref involvedComponents: Component[*];
	}

 	occurrence def Mission :> ArcadiaElement {
	    private doc /* A  generic Arcadia Capability, used across all Arcadia engineering perspectives. */
		ref involvedComponents: Component[*];
		ref exploitedCapabilties: Capability[*];
	}

}
