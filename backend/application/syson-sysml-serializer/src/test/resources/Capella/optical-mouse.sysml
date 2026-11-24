/*
 * Sample model of an optical mouse system using the Arcadia method in a SysML v2 structure.
 * 
 * This package defines the Logical Architecture of the system, including:
 * - Functional decomposition: capturing and processing optical movement data.
 * - Component allocation: mapping functions to the Optical Sensor Unit and Mouse Controller components.
 * - Interface definition: establishing a component exchange ("cable") linked to functional flows.
 * - Requirement traceability: capturing system-level expectations (e.g., optical movement detection).
 * 
 * The model illustrates how to use Arcadia engineering perspectives in SysML v2 to ensure
 * traceability between functions, components, exchanges, and requirements.
 */
package <SE>'System Engineering' {
    private import Arcadia::*;
    private import ScalarValues::Boolean;
	private import ModelingMetadata::*;
	private import KerML::FeatureDirectionKind;

   	metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::SystemEngineering;}

	package <OA> 'Operational Analysis' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::OperationalAnalysis;}

		doc /* Defines the operational view of the system context and stakeholder needs.
Captures missions, actors, and activities without technical constraints.
Serves as the foundation for deriving system-level requirements. */

		package Functions;
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Functions::*;
		}
        package Requirements;
	}
	package <SA> 'System Analysis' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::SystemAnalysis;}

		doc /* Defines the system’s intended behavior and responsibilities in response to stakeholder needs.
Describes system functions, interfaces, and interactions, independently from implementation choices.
Serves as a basis for the logical architecture. */

		package Functions;
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Functions::*;
		}
        package Requirements;
	}
	package <LA> 'Logical Architecture' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::LogicalArchitecture;}

		doc /* Defines the logical architecture of the system.
Contains logical components, functions, and their interactions, abstracted from implementation details.
Serves as a bridge between system needs and physical design. */

		package Functions{
			   	    	action 'Capture Movement' : Function {
        		:>> description = "Interpret raw optical data into motion vectors.";
            	@StatusInfo {status = StatusKind::tbd;}
            	out rawOpticalData : ExchangeItem;
        	}
        	
   	    	action 'Transform Raw Optical Data' : Function {
   	    		in rawOpticalData : ExchangeItem;
   	    	}
   	    	
   	    	item rawOpticalData: ExchangeItem;
   	    	
   	    	// A Functional Exchange with an allocated Exchange Item
   	    	flow 'Optical Data' : FunctionalExchange of rawOpticalData 
   	    		from 'Capture Movement'.rawOpticalData to 'Transform Raw Optical Data'.rawOpticalData;
   	    	
   	    	// A Functional Chain	
   	    	action 'read movement' : FunctionalChain {
   	    		ref :>> involvedFunctionalExchanges = ('Optical Data');
   	    	} 
		}
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Functions::*;
			part 'Optical Sensor Unit' : Component {
			    :>> description = "Captures surface movement using optical detection.";
                :>> isActor = false;
                :>> isHuman = false;
                perform 'Capture Movement';
                port 'Cable Conector' : ComponentPort  {
	               	out item rawOpticalData : ExchangeItem;
	               	:>> direction = FeatureDirectionKind::'out';
	               	//TODO Find out how we can allocate Functions Parameters (Fonctional Ports in Capella) to Component Ports
	               	//ref Feature[*] :>> allocatedFunctionPorts = ('Capture Movement'.rawOpticalData);
                } 
		 	}

			part 'Mouse Controller' : Component {
                :>> description = "Manages mouse peripherals";
                :>> isActor = false;
                :>> isHuman = false;
                perform 'Transform Raw Optical Data';
                port 'Cable Conector' : ComponentPort {
	               	in item rawOpticalData : ExchangeItem;
	               	:>> direction = FeatureDirectionKind::'in';
 	               	//TODO Find out how we can allocate Functions Parameters (Fonctional Ports in Capella) to Component Ports
	               	//ref Feature[*] :>> allocatedFunctionPorts = ('Transform Raw Optical Data'.rawOpticalData);
                } 
		 	}
		
			// An interface between 2 compoenents Ports, with a flow between these 2 ports (related to Functional Exchange to Component Exchange allocation)
			interface cable : ComponentExchange
				connect 'Optical Sensor Unit'.'Cable Conector' to 'Mouse Controller'.'Cable Conector'
				{
				flow 'Optical Sensor Unit'.'Cable Conector' to 'Mouse Controller'.'Cable Conector';
		        ref :>> allocatedExchangeItems = (rawOpticalData);
        		ref :>> allocatedFunctionalExchanges = ('Optical Data');

			}
			
			// A Fonctional Exchange allocated to a Component Exchange
			allocate 'Optical Data' to cable;
		}
        package Requirements;
	}
	package <PA> 'Physical Architecture' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::PhysicalArchitecture;}

		doc /* Defines the physical components and their interactions that realize the logical architecture.
Includes hardware, software, and communication elements, with consideration for implementation constraints.
Supports integration, validation, and preparation for deployment. */

		package Functions;
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Functions::*;
		}
        package Requirements{
			//R1.1 is the ID of the requirement
			requirement <'R1.1'> Optical : ArcadiaRequirement {
				doc /*
				 * The mouse should capture movement using optical detection
				 */
                :>> description = "Context/description for the requirement";
			}
		}
	}
    package <EPBS> 'EPBS Architecture' {
		metadata ArcadiaPackage {engineeringPerspective = ArcadiaEngineeringPerspective::EPBS;}

		doc /* Defines the configuration of the system as delivered to the customer.
Describes the breakdown of end products, including deliverable items, versions, and traceability to physical components and requirements.
Supports verification, acceptance, and product management. */

		package Functions;
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Functions::*;
		}
        package Requirements;
	}
}