// Source https://github.com/MBSE4U/dont-panic-batmobile/tree/main
// License Apache-2.0 license
package Batmobile {
    doc
    /*
     * The book "Don't Panic - The Absolute Beginners Guide to SysML v2" by Tim Weilkiens and Christian Muggeo 
     * uses the Batmobile as a fictional example. 
     *
     * The Batmobile example in this book was inspired by the legendary lecture Jon Holt gave dressed in a Batman costume.
     *
     * The book provides an overview of SysML v2, so it is a small example with many snippets shown throughout the book. 
     * There is therefore also no sophisticated package structure. All elements are in one package.
     */

    //
    // Common Definitions
    //

    // Common vehicle definition
    part def Vehicle {
        item driver;
        part engine;
        attribute actualSpeed :> ISQ::speed;
    }

    // Interface for a power connection
    interface def PowerInterface {
        end supplierPort : PowerIP;
        end consumerPort : ~PowerIP;
        flow of Power from supplierPort.power to consumerPort.power;
    }
    // Interaction point for power
    port def PowerIP {
        out item power : Power;
    }
    // Definition of power
    item def Power {
        attribute value;
    }

    // Definition of a wheel with a bounding box
    part def Wheel {
        item boundingBox : ShapeItems::Box [1] :> boundingShapes {
            attribute :>> length :> ISQ::length = 80 [SI::cm];
            attribute :>> width :> ISQ::length = 26 [SI::cm];
            attribute :>> height :> ISQ::length = 80 [SI::cm];
        }
        attribute pressure;
    }

    /// Definition of an engine for the Batmobile
    part def BatmobileEngine {
        perform action rocketBoost {
            in cmd : EngineCommand = engineControl.cmd;
        }
        port engineControl {
            in item cmd : EngineCommand;
        }
    }

    // Definition of commands to control the engine
    item def EngineCommand;

    //
    // The Batmobile
    //
    part def Batmobile :> Vehicle, System {
        :>> problemStatement := "As a Hero, Batman needs a cool vehicle.";
        :>> systemIdea := "The Batmobile is a black vehicle...";

        part seat [2];
        part body;
        part wheels : Wheel [4];
        part frontLeftWheel :> wheels;
        part frontRightWheel :> wheels;
        part rearLeftWheel :> wheels;
        part rearRightWheel :> wheels;

        part battery {
            port powerPort : PowerIP;
            attribute capacity;
        }
        part batmobileEngine : BatmobileEngine :>> engine {
            port enginePort : ~PowerIP;
        }
        interface bat2eng : PowerInterface connect battery.powerPort to batmobileEngine.enginePort;
    }

    // A special version: the Batmobile Next Generation
    part def BatmobileNG :> Batmobile {
        part bulletProofWheels :>> wheels [4] {
            part metalCoating;
        }
    }

    //
    // Batman
    //
    individual item def Batman :> Hero {
        attribute realName = "Jon Holt";
    }
    item def Hero;

    //
    // Timeslices
    //

    // Since we introduced the language extension "system" and used it for the definition of Batmobile, the part must also use the extension.
    // In the book, "bm1" is just a part, and Batmobile a part definition except in the paragraph where we explain the language extension.
    part bm1 : Batmobile {
        timeslice batmanDriving {
            item :>> driver : Batman;
            snapshot :>> start {
                attribute :>> battery.capacity = 40000 [SI::'watt hour'];
            }
            snapshot :>> done {
                attribute :>> battery.capacity = 42 [SI::'watt hour'];
            }
        }
        then timeslice charging {
            item :>> driver = null;
        }
    }

    //
    // Behavior modeling
    //
    action def 'Drive Batmobile' {
        first start;
        then action startBatmobile;
        then action scanEnvironment {
            out status : StatusKind;
        }
        then decide;
            if scanEnvironment.status == StatusKind::safe then 'Switch to standard mode';
            if scanEnvironment.status == StatusKind::alert then 'Switch to alert mode';
        action 'Switch to standard mode';
        then endOfStatusCheck;
        action 'Switch to alert mode';
        then endOfStatusCheck;
        merge endOfStatusCheck;
        then done;
    }
    enum def StatusKind {
        enum safe;
        enum alert;
    }

    //
    // Use Cases
    //
    use case def 'Activate rocket booster' {
        subject bm : Batmobile;
        actor driver : Batman;
        objective {
            doc
            /*
             * The driver wants to activate 
             * the rocket booster to increase 
             * the speed extremely.
             */
        }
    }
    action def ActivateRocketBooster :> 'Activate rocket booster' {
        first start;
        then action prepareBoost;
        then action activateBoost;
        then done;
    }

    //
    // Requirements
    //

    requirement def VehicleMaxSpeed {
        doc
        /*
         * The actual speed of the vehicle shall 
         * be less or equal than the maximum speed.
         */
        subject vehicle : Vehicle;
        stakeholder pm : ProductManagement;
        attribute maxSpeed :> ISQ::speed;
        require constraint { vehicle.actualSpeed <= maxSpeed }
    }
    item def ProductManagement;

    requirement batmobileSpecification {
        requirement <REQ42> batmobileMaxSpeed : VehicleMaxSpeed {
            attribute :>> maxSpeed = 230 [SI::'km/h'];
        }
        requirement <REQ43> batmobileAcceleration;
    }

    part batmobileDesignV23 : Batmobile;
    satisfy batmobileSpecification by batmobileDesignV23;

    //
    // Language Extension
    //
    part def System {
        doc
        /*
         * Represents an engineered system that is a system designed or adapted 
         * to interact with an anticipated operational environment to achieve one or more 
         * intended purposes while complying with applicable constraints.
         */
        attribute problemStatement : ScalarValues::String {
            doc
            /*
             * The problem solved by the system.
             */
        }
        attribute systemIdea : ScalarValues::String {
            doc
            /*
             * The elevator pitch of how the system solves the problem.
             */
        }
    }
    part systems : System [*];

    //
    // Viewpoint and View
    //
    part def HeroAssociation;
    concern 'Reduce the number of special parts' {
        doc
        /*
         * Reduce the number of special parts to reduce the dependency
         * to special suppliers and experts.
         */
        stakeholder heroAss : HeroAssociation;
    }

    viewpoint 'system components' {
        frame 'Reduce the number of special parts';
        require constraint {
            doc
            /*
             * A system components view shall show the hierarchical
             * part decomposition of a system, starting with a
             * specified root part.
             */
        }
    }

    view def 'Part list' {
        satisfy 'system components';
        filter @ SysML::PartUsage;
    }

    view batmobileParts : 'Part list' {
        expose Dont_Panic_Batmobile::**;
        render Views::asElementTable;
    }

    //
    // Variability Modeling
    //

    // Shared Assets Library
    part def StandardEngine :> BatmobileEngine;
    part def XtremeEngine :> BatmobileEngine;

    part def StandardWheel :> Wheel;
    part def XtremeWheel :> Wheel;

    // Variability
    variation part def EngineChoices :> BatmobileEngine {
        variant part sEngine : StandardEngine;
        variant part xEngine : XtremeEngine;
    }
    variation part def WheelChoices :> Wheel {
        variant part sWheel : StandardWheel;
        variant part xWheel : XtremeWheel;
    }

    // Configuration
    part def BatmobileConfigurations :> Batmobile {
        part :>> batmobileEngine : EngineChoices;
        part :>> wheels [4] : WheelChoices;

        assert constraint {
            batmobileEngine == batmobileEngine::xEngine and wheels == wheels.xWheel
        }
    }

    // Member Product
    part def XBatmobile :> BatmobileConfigurations {
        part :>> batmobileEngine = batmobileEngine.xEngine;
        part :>> wheels = wheels.xWheel;
    }

    view 'view1' : StandardViewDefinitions::GeneralView {
		expose Vehicle;
		expose Vehicle::driver;
		expose Vehicle::engine;
		expose PowerInterface;
		expose PowerIP;
		expose PowerIP::power;
		expose Power;
		expose Wheel;
		expose Wheel::boundingBox;
		expose BatmobileEngine;
		expose BatmobileEngine::rocketBoost;
		expose EngineCommand;
		expose Batmobile;
		expose Batmobile::seat;
		expose Batmobile::body;
		expose Batmobile::wheels;
		expose Batmobile::frontLeftWheel;
		expose Batmobile::frontRightWheel;
		expose Batmobile::rearLeftWheel;
		expose Batmobile::rearRightWheel;
		expose Batmobile::battery;
		expose Batmobile::batmobileEngine;
		expose Batmobile::bat2eng;
		expose BatmobileNG;
		expose BatmobileNG::bulletProofWheels;
		expose BatmobileNG::bulletProofWheels::metalCoating;
		expose Batman;
		expose Hero;
		expose bm1;
		expose bm1::batmanDriving;
		expose bm1::charging;
		expose 'Drive Batmobile';
		expose StatusKind;
		expose 'Activate rocket booster';
		expose 'Activate rocket booster'::driver;
		expose ActivateRocketBooster;
		expose VehicleMaxSpeed;
		expose ProductManagement;
		expose batmobileSpecification;
		expose batmobileSpecification::batmobileMaxSpeed;
		expose batmobileSpecification::batmobileAcceleration;
		expose batmobileDesignV23;
		expose System;
		expose systems;
		expose HeroAssociation;
		expose 'Reduce the number of special parts';
		expose StandardEngine;
		expose XtremeEngine;
		expose StandardWheel;
		expose XtremeWheel;
		expose EngineChoices;
		expose WheelChoices;
		expose BatmobileConfigurations;
		expose BatmobileConfigurations::batmobileEngine;
		expose BatmobileConfigurations::wheels;
		expose XBatmobile;
		expose XBatmobile::batmobileEngine;
		expose XBatmobile::wheels;
	}
}