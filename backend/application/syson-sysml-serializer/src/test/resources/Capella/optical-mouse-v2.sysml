/*7
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

part def <SE> OpticalMouseModel {
	metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::SystemEngineering;}
    private import Arcadia::*;
    private import ScalarValues::Boolean;
	private import ModelingMetadata::*;
	private import KerML::FeatureDirectionKind; 

	part def <OA> 'Operational Analysis' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::OperationalAnalysis;}
		doc /* Defines the operational view of the system context and stakeholder needs.
		Captures missions, actors, and activities without technical constraints.
		Serves as the foundation for deriving system-level requirements. */
		package Functions {
			private import Data::*;	
			action 'Root Function' : Function;
		}
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Data::*;	
			private import Functions::*;
			part system : Component;
		}
		package Requirements;
	}

	part def <SA> 'System Analysis' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::SystemAnalysis;}
		doc /* Defines the system’s intended behavior and responsibilities in response to stakeholder needs.
		Describes system functions, interfaces, and interactions, independently from implementation choices.
		Serves as a basis for the logical architecture. */
		package Functions {
			private import Data::*;	
			action 'Root Function' : Function;
		}
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Data::*;	
			private import Functions::*;
			part system : Component;
		}
		package Requirements;
	}
    		
	part def <LA> 'Logical Architecture' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::LogicalArchitecture;}
		doc /* Defines the logical architecture of the system.
		Contains logical components, functions, and their interactions, abstracted from implementation details.
		Serves as a bridge between system needs and physical design. */
		package Functions {
			private import Data::*;			
			action 'Root Function' : Function {
	   	    	action 'Capture Movement' : Function {
	   	    		//Corresponds to the description field in Capella
	        		doc description /*Interpret raw optical data into motion vectors.*/
	            	@StatusInfo {status = StatusKind::tbd;}
	            	//Correspond to a Capella Function Output Port and the allocated Exchange Item(s) (binding rawOpticalData) 
	            	out FOP1 [0..*] : ExchangeItem = (rawOpticalData);
	        	}
	        	
	   	    	action 'Transform Raw Optical Data' : Function {
	   	    		in FIP2 [0..*] : ExchangeItem = (rawOpticalData);
	   	    	}
	
	   	    	// A Functional Exchange
	   	    	flow 'Optical Data' : FunctionalExchange of ExchangeItem[*]
	   	    		from 'Capture Movement'.FOP1 to 'Transform Raw Optical Data'.FIP2;
	   	    	// Binding the Exchange Item(s) to the flow
	   	    	bind 'Optical Data'.payload = rawOpticalData;
	   	    	
	   	    
	   	    	// A Functional Chain	
	   	    	action 'read movement' : FunctionalChain {
	   	    		ref :>> involvedFunctionalExchanges = ('Optical Data');
	   	    		first start;
	   	    		then 'Root Function'.'Capture Movement';
	 	    		then 'Root Function'.'Transform Raw Optical Data';
	 	    		then done;
	   	    	}
	   	    }
    	}

		package Capabilities {
			private import Functions::*;			
			private import Structure::*;			
			occurrence 'Move Cursor on Screen' : Capability  {
				ref :>> involvedFunctions = ('Root Function'.'Capture Movement', 'Root Function'.'Transform Raw Optical Data');
				ref :>> involvedFunctionalChains = ('Root Function'.'read movement');
				ref :>> involvedComponents = (system.'Optical Sensor Unit', system.'Mouse Controller');
			}
			
			occurrence 'Provide Interface to User' : Mission  {
				ref :>> exploitedCapabilties = ('Move Cursor on Screen');
				ref :>> involvedComponents = (system.'Optical Sensor Unit', system.'Mouse Controller');
			}
			
		}
		package Interfaces;
		package Data {
   	    	item rawOpticalData: ExchangeItem;
		}
		package Structure {
			private import Functions::*;	
			private import Data::*;	
			part computerUser : Component {
				:>> isActor = true;
			}
				
			part system : Component{
				part 'Optical Sensor Unit' : Component {
				    doc description /*Captures surface movement using optical detection.*/
	                :>> isActor = false;
	                :>> isHuman = false;
	                perform 'Root Function'.'Capture Movement';
	                port 'Cable Conector' : ComponentPort  {
		               	out [0..*] : ExchangeItem = (rawOpticalData);               	
		               	:>> direction = FeatureDirectionKind::'out';
		               	ref :>> allocatedFunctionPorts = ('Capture Movement'.FOP1);
	                } 
			 	}
	
				part 'Mouse Controller' : Component {
	                doc description /*Manages mouse peripherals*/
	                :>> isActor = false;
	                :>> isHuman = false;
	                perform 'Root Function'.'Transform Raw Optical Data';
	                port 'Cable Conector' : ComponentPort {
		               	in [0..*] : ExchangeItem = (rawOpticalData);               	
		               	:>> direction = FeatureDirectionKind::'in';
		               	ref :>> allocatedFunctionPorts = ('Transform Raw Optical Data'.FIP2);
	                } 
			 	}
			
				// An interface between 2 compoenents Ports
				interface cable : ComponentExchange
					connect 'Optical Sensor Unit'.'Cable Conector' to 'Mouse Controller'.'Cable Conector' {
					flow system.'Optical Sensor Unit'.'Cable Conector' to system.'Mouse Controller'.'Cable Conector';
			        ref :>> allocatedExchangeItems = (rawOpticalData);
	        		ref :>> allocatedFunctionalExchanges = ('Root Function'.'Optical Data');
					}
				
				// A Fonctional Exchange allocated to a Component Exchange
				allocation : ExchangeAllocation
					allocate 'Root Function'.'Optical Data' to cable;
			}
		}

		package Requirements {
			//R1.1 is the ID of the requirement
			requirement <'R1.1'> Optical : ArcadiaRequirement {
			// TODO: discuss shall statement versus description field
				doc /*
				 * The mouse should capture movement using optical detection
				 */
			}
		}
	}
	
	part def <PA> 'Physical Architecture' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::PhysicalArchitecture;}
		doc /* Defines the physical components and their interactions that realize the logical architecture.
		Includes hardware, software, and communication elements, with consideration for implementation constraints.
		Supports integration, validation, and preparation for deployment. */
		package Functions {
			private import Data::*;	
			action 'Root Function' : Function;
		}
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Data::*;	
			private import Functions::*;
			part system : Component{
			
			//TODO SLA: Create NodeComponent and BehavioralComponent...
				part 'Optical Sensor Module' : Component {
				    doc description /*Captures surface movement using optical detection.*/
	                :>> isActor = false;
	                :>> isHuman = false;
	            }
	        }
		}
		package Requirements;
	}
	
	part def <EPBS> 'EPBS Architecture' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::EPBS;}
		doc /* Defines the configuration of the system as delivered to the customer.
		Describes the breakdown of end products, including deliverable items, versions, and traceability to physical components and requirements.
		Supports verification, acceptance, and product management. */
		package Functions {
			private import Data::*;	
			action 'Root Function' : Function;
		}
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Data::*;	
			private import Functions::*;
			part system : Component;
		}
		package Requirements;
	}
}
