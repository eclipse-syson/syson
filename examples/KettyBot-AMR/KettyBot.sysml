/*******************************************************************************
 * KettyBot-like AMR (Autonomous Mobile Robot) system model.
 *
 * Modeled in OMG SysML v2, inspired by the Pudu Robotics KettyBot / KettyBot Pro
 * delivery & reception robot (publicly available product specifications).
 *
 * This is an independent engineering model created for learning/demonstration
 * purposes inside Eclipse SysON. "KettyBot" and "PUDU" are trademarks of their
 * respective owners; this model is not affiliated with or endorsed by Pudu Robotics.
 *
 * Reference product specifications used to derive the model:
 *   - Dimensions : 435 x 450 x 1120 mm (W x D x H)
 *   - Weight     : 38 kg
 *   - Payload    : 30 kg total (10 kg per tray), 2 trays + 1 dish-return box
 *   - Battery    : 20 Ah @ 25.6 V DC
 *   - Run time   : 8-9 h (no load)
 *   - Charge time: 4 h (manual + auto)
 *   - Speed      : 0.2 - 1.2 m/s (adjustable)
 *   - Navigation : Laser + visual integrated SLAM
 *   - Min. aisle : 52-55 cm
 *   - Threshold  : 5 mm max
 *   - Climb angle: <= 5 deg
 *   - Screens    : 10.1" interaction LCD + 18.5" advertising LCD
 *   - Audio      : 6-mic circular array + 2 x 10 W stereo speakers
 *   - OS         : Android
 *   - Environment: 0-40 degC, RH <= 85%, indoor, IP20
 *   - Modes      : Guiding, Birthday, Customer attraction, General delivery,
 *                  Delivery, Advertising, Cruise, Dish-return, Table service
 ******************************************************************************/
package KettyBotAMR {
    import ScalarValues::*;
    import ISQ::*;
    import SI::*;

    doc
    /*
     * Engineering model of an Autonomous Mobile Robot (AMR) for indoor
     * delivery and reception tasks (restaurants, hotels, retail), inspired by
     * the Pudu Robotics KettyBot.
     *
     * The robot combines laser + visual SLAM navigation, multi-tray payload,
     * an advertising display, AI voice interaction and automatic recharging.
     */

    //--------------------------------------------------------------------------
    // Language extension: a generic Engineered System
    //--------------------------------------------------------------------------
    part def System {
        doc /* An engineered system designed to achieve one or more purposes. */
        attribute problemStatement : String;
        attribute systemIdea : String;
    }

    //--------------------------------------------------------------------------
    // Common definitions
    //--------------------------------------------------------------------------

    // Base definition for any autonomous mobile robot.
    part def MobileRobot {
        attribute mass :> Mass::mass;
        attribute width :> length;
        attribute depth :> length;
        attribute height :> length;
        attribute actualSpeed :> speed;
        attribute maxSpeed :> speed;
    }

    // Geometric bounding box used for sizing / clearance analysis.
    item def BoundingBox {
        attribute length :> ISQ::length;
        attribute width  :> ISQ::length;
        attribute height :> ISQ::length;
    }

    //--------------------------------------------------------------------------
    // Locomotion & power subsystems
    //--------------------------------------------------------------------------
    part def DriveWheel {
        item boundingBox : BoundingBox [1];
        attribute diameter :> length = 150 [mm];
    }

    part def CasterWheel {
        item boundingBox : BoundingBox [1];
        attribute diameter :> length = 90 [mm];
    }

    part def DriveMotor {
        attribute nominalVoltage :> Voltage::electricPotential = 25.6 [V];
        attribute ratedPower :> Power::power;
    }

    // Rechargeable battery pack powering the whole robot.
    part def BatteryPack {
        attribute nominalVoltage :> Voltage::electricPotential = 25.6 [V];
        attribute capacity :> ElectricCharge::electricCharge = 20 [Ah];
        attribute chargeTime :> Time::time = 4 [h];
        attribute runTimeNoLoad :> Time::time = 8 [h];
        attribute stateOfCharge : Rational = 100; // percent 0..100
        attribute energyCapacity :> Energy :> ISQ::energy = 512 ['W*h'];
    }

    // Autonomous docking charging station.
    part def ChargingStation {
        attribute outputVoltage :> Voltage::electricPotential = 29 [V];
        attribute outputCurrent :> Current::electricCurrent = 8 [A];
        attribute inputVoltageACMin :> Voltage::electricPotential = 100 [V];
        attribute inputVoltageACMax :> Voltage::electricPotential = 240 [V];
    }

    //--------------------------------------------------------------------------
    // Perception (sensors for SLAM navigation & obstacle avoidance)
    //--------------------------------------------------------------------------
    part def LidarSensor {
        attribute scanRate :> Frequency::frequency = 5400 ['1/min'];
        doc /* Laser SLAM sensor; up to 5400 obstacle-detection sweeps per minute. */
    }

    part def RGBDCamera {
        attribute purpose : String = "Visual SLAM and obstacle avoidance";
    }

    part def FisheyeCamera360 {
        attribute purpose : String = "360 deg tray-load detection";
    }

    part def IMU {
        attribute purpose : String = "Orientation and odometry";
    }

    //--------------------------------------------------------------------------
    // Payload & HMI subsystems
    //--------------------------------------------------------------------------
    part def Tray {
        item boundingBox : BoundingBox [1] {
            attribute :>> length = 352 [mm];
            attribute :>> width  = 340 [mm];
        }
        attribute maxLoad :> Mass::mass = 10 [kg];
        attribute loadPresent : Boolean = false;
    }

    part def DishReturnBox {
        attribute purpose : String = "Dish return / bussing compartment";
    }

    part def InteractionScreen {
        attribute screenSize :> length = 10.1 ['in'];
        attribute technology : String = "LCD touch";
    }

    part def AdvertisingScreen {
        attribute screenSize :> length = 18.5 ['in'];
        attribute technology : String = "LCD";
        attribute orientation : String = "vertical";
    }

    part def Speaker {
        attribute channelCount = 2;
        attribute powerPerChannel :> Power::power = 10 [W];
    }

    part def MicrophoneArray {
        attribute elementCount = 6;
        attribute topology : String = "circular array";
    }

    //--------------------------------------------------------------------------
    // The AMR robot (KettyBot-like)
    //--------------------------------------------------------------------------
    part def KettyBot :> MobileRobot, System {
        :>> problemStatement := "Restaurants and hotels need a compact robot that can autonomously deliver items, greet and escort guests, and display advertising.";
        :>> systemIdea := "A compact AMR using laser + visual SLAM, multi-tray payload, an advertising display, AI voice interaction and automatic recharging.";

        // Physical envelope (KettyBot specifications).
        :>> mass   = 38 [kg];
        :>> width  = 435 [mm];
        :>> depth  = 450 [mm];
        :>> height = 1120 [mm];
        :>> maxSpeed = 1.2 ['m/s'];

        // Structural parts
        part chassis;
        part body {
            attribute material : String = "ABS / aviation-grade aluminium alloy";
        }

        // Locomotion
        part driveWheels : DriveWheel [2];
        part casterWheels : CasterWheel [2];
        part driveMotors : DriveMotor [2];

        // Power
        part battery : BatteryPack {
            port powerPort : PowerOut;
        }
        part chargingConnector;

        // Navigation & perception
        part lidar : LidarSensor;
        part rgbdCameras : RGBDCamera [*];
        part fisheyeCamera : FisheyeCamera360 [2];
        part imu : IMU;
        part navigationController {
            attribute method : String = "Laser + visual integrated SLAM";
            attribute planner : String = "PUDU Scheduler (multi-robot cooperation)";
        }

        // Payload
        part trays : Tray [2];
        part dishReturn : DishReturnBox [1];

        // HMI
        part interactionScreen : InteractionScreen;
        part adScreen : AdvertisingScreen;
        part speakers : Speaker [2];
        part microphone : MicrophoneArray;
        part computeUnit {
            attribute operatingSystem : String = "Android";
        }

        // Mission state
        attribute operatingMode : KettyBotAMR::MissionMode = KettyBotAMR::MissionMode::delivery;
        attribute minAisleWidth :> length = 550 [mm];
        attribute maxClimbAngle :> PlaneAngle::planeAngle = 5 ['deg'];
        attribute maxThresholdHeight :> length = 5 [mm];
    }

    // Outbound power port for the battery.
    port def PowerOut {
        out item power : ElectricPower;
    }
    item def ElectricPower {
        attribute value;
    }

    //--------------------------------------------------------------------------
    // Mission modes (the 9 KettyBot function modes)
    //--------------------------------------------------------------------------
    enum def MissionMode {
        enum guiding;
        enum birthday;
        enum customerAttraction;
        enum generalDelivery;
        enum delivery;
        enum advertising;
        enum cruise;
        enum dishReturn;
        enum tableService;
    }

    //--------------------------------------------------------------------------
    // Behavior modeling
    //--------------------------------------------------------------------------
    action def ExecuteDeliveryMission {
        in item destination;
        first start;
        then action receiveOrder;
        then action planPath {
            out path;
        }
        then action navigateToPickup;
        then action loadTrays;
        then action navigateToTable;
        then action unloadTrays;
        then action checkBattery;
        then decide;
            if checkBattery.low then 'Return to charging station';
            if checkBattery.low == false then 'Await next order';
        action 'Return to charging station';
        then dock;
        action 'Await next order';
        then merge done;
    }

    action def CruiseAndAdvertise {
        doc /* Cruise a defined area offering items while broadcasting ads. */
        first start;
        then action cruiseLoop;
        then action engageCustomer;
        then done;
    }

    //--------------------------------------------------------------------------
    // Use Cases
    //--------------------------------------------------------------------------
    use case def 'Deliver items to table' {
        subject robot : KettyBot;
        actor guest;
        objective {
            doc /* The robot shall autonomously deliver ordered items to the guest's table. */
        }
    }

    use case def 'Greet and escort guest' {
        subject robot : KettyBot;
        actor guest;
        objective {
            doc /* The robot shall greet a guest and lead them to a table using optimal path planning. */
        }
    }

    use case def 'Auto-recharge when low' {
        subject robot : KettyBot;
        objective {
            doc /* When the state of charge falls below 10%, the robot shall navigate to its charging station. */
        }
    }

    //--------------------------------------------------------------------------
    // Requirements
    //--------------------------------------------------------------------------
    requirement def MaxSpeedRequirement {
        doc /* The robot's actual speed shall not exceed its maximum speed. */
        subject robot : MobileRobot;
        attribute limit :> speed;
        require constraint { robot.actualSpeed <= limit }
    }

    requirement def PayloadCapacityRequirement {
        doc /* Total carried payload shall not exceed the rated capacity. */
        subject robot : KettyBot;
        attribute maxPayload :> Mass::mass;
        require constraint { robot.trays.maxLoad and robot.dishReturn <= maxPayload }
    }

    requirement def BatteryLifeRequirement {
        doc /* The robot shall operate at least the specified duration on a full charge (no load). */
        subject battery : BatteryPack;
        attribute minRunTime :> Time::time;
        require constraint { battery.runTimeNoLoad >= minRunTime }
    }

    requirement def AisleWidthRequirement {
        doc /* The robot shall be able to pass through the minimum specified aisle width. */
        subject robot : KettyBot;
        attribute maxAisleWidth :> length;
        require constraint { robot.minAisleWidth <= maxAisleWidth }
    }

    requirement def ObstacleDetectionRequirement {
        doc /* The perception system shall detect obstacles at the required rate. */
        subject sensor : LidarSensor;
        attribute minScanRate :> Frequency::frequency;
        require constraint { sensor.scanRate >= minScanRate }
    }

    requirement def OperatingEnvironmentRequirement {
        doc /* The robot shall operate within the specified temperature range. */
        subject robot : KettyBot;
        attribute minTemp :> ThermodynamicTemperature::temperature;
        attribute maxTemp :> ThermodynamicTemperature::temperature;
        attribute maxHumidity : Rational; // percent
        require constraint { minTemp >= 0 ['degC'] and maxTemp <= 40 ['degC'] and maxHumidity <= 85 }
    }

    requirement kettyBotSpecification {
        requirement <KB-SPD-01> maxSpeed : MaxSpeedRequirement {
            attribute :>> limit = 1.2 ['m/s'];
        }
        requirement <KB-PAY-02> payload : PayloadCapacityRequirement {
            attribute :>> maxPayload = 30 [kg];
        }
        requirement <KB-BAT-03> batteryLife : BatteryLifeRequirement {
            attribute :>> minRunTime = 8 [h];
        }
        requirement <KB-AIS-04> aisleWidth : AisleWidthRequirement {
            attribute :>> maxAisleWidth = 0.55 [m];
        }
        requirement <KB-OBS-05> obstacleDetection : ObstacleDetectionRequirement {
            attribute :>> minScanRate = 5400 ['1/min'];
        }
        requirement <KB-ENV-06> environment : OperatingEnvironmentRequirement {
            attribute :>> minTemp = 0 ['degC'];
            attribute :>> maxTemp = 40 ['degC'];
            attribute :>> maxHumidity = 85;
        }
    }

    // A design instance that satisfies the specification.
    part kettyBotDesignV1 : KettyBot;

    satisfy kettyBotSpecification by kettyBotDesignV1;

    //--------------------------------------------------------------------------
    // Mission timeslice example: a delivery run from full to low battery
    //--------------------------------------------------------------------------
    part kb1 : KettyBot {
        timeslice deliveryRun {
            snapshot :>> start {
                attribute :>> battery.stateOfCharge = 100;
            }
            snapshot :>> done {
                attribute :>> battery.stateOfCharge = 15;
            }
        }
        then timeslice autoRecharge {
            :>> operatingMode = KettyBotAMR::MissionMode::delivery;
        }
    }

    //--------------------------------------------------------------------------
    // General view (importable as a SysON diagram)
    //--------------------------------------------------------------------------
    view 'KettyBot General View' : StandardViewDefinitions::GeneralView {
        expose KettyBot;
        expose KettyBot::chassis;
        expose KettyBot::body;
        expose KettyBot::driveWheels;
        expose KettyBot::casterWheels;
        expose KettyBot::driveMotors;
        expose KettyBot::battery;
        expose KettyBot::chargingConnector;
        expose KettyBot::lidar;
        expose KettyBot::rgbdCameras;
        expose KettyBot::fisheyeCamera;
        expose KettyBot::imu;
        expose KettyBot::navigationController;
        expose KettyBot::trays;
        expose KettyBot::dishReturn;
        expose KettyBot::interactionScreen;
        expose KettyBot::adScreen;
        expose KettyBot::speakers;
        expose KettyBot::microphone;
        expose KettyBot::computeUnit;
        expose BatteryPack;
        expose DriveWheel;
        expose CasterWheel;
        expose DriveMotor;
        expose LidarSensor;
        expose RGBDCamera;
        expose FisheyeCamera360;
        expose IMU;
        expose Tray;
        expose DishReturnBox;
        expose InteractionScreen;
        expose AdvertisingScreen;
        expose Speaker;
        expose MicrophoneArray;
        expose ChargingStation;
        expose MissionMode;
        expose ExecuteDeliveryMission;
        expose CruiseAndAdvertise;
        expose 'Deliver items to table';
        expose 'Greet and escort guest';
        expose 'Auto-recharge when low';
        expose kettyBotSpecification;
        expose kettyBotDesignV1;
        expose kb1;
        expose kb1::deliveryRun;
        expose kb1::autoRecharge;
    }
}
