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

		package Functions;
		package Capabilities;
		package Interfaces;
		package Data;
		package Structure {
			private import Functions::*;
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
        package Requirements;
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