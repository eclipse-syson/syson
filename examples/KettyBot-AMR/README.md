# KettyBot-like AMR — SysML v2 model

A SysML v2 engineering model of an **Autonomous Mobile Robot (AMR)** for indoor
delivery & reception, inspired by the **Pudu Robotics KettyBot**.

This folder contains a self-contained `.sysml` file you can import into
**Eclipse SysON** as a new project. It exercises most SysML v2 constructs that
SysON supports: parts, attributes with SI units, ports, actions (with control
flow), use cases, requirements (with satisfaction), enumerations, snapshots/
timeslices, and a general view.

## Contents of the model

| Section | What it models |
|---------|----------------|
| `System` | Generic language extension (problem statement / system idea) |
| `MobileRobot` | Base definition: mass, dimensions, speed |
| Locomotion | `DriveWheel`, `CasterWheel`, `DriveMotor` |
| Power | `BatteryPack` (20 Ah / 25.6 V), `ChargingStation`, `PowerOut` port |
| Perception | `LidarSensor`, `RGBDCamera`, `FisheyeCamera360`, `IMU` |
| Payload & HMI | `Tray` (2×), `DishReturnBox`, `InteractionScreen` (10.1"), `AdvertisingScreen` (18.5"), `Speaker`, `MicrophoneArray` |
| `KettyBot` | The assembled robot with full subsystem decomposition |
| `MissionMode` | Enum of the 9 KettyBot function modes |
| Behavior | `ExecuteDeliveryMission`, `CruiseAndAdvertise` (action flow) |
| Use cases | Deliver items, Greet & escort, Auto-recharge when low |
| Requirements | Max speed, payload, battery life, aisle width, obstacle detection, operating environment |
| View | `KettyBot General View` (renders as a SysON diagram) |

All numeric values are derived from publicly available KettyBot / KettyBot Pro
specifications (dimensions 435×450×1120 mm, 38 kg, 30 kg payload, 1.2 m/s, 8 h
run time, 55 cm aisle, etc.).

## How to use it in SysON

1. Start SysON (e.g. `docker compose up` from the repository root, then open
   <http://localhost:8080>).
2. Create a new project and choose **Import** a SysML textual model.
3. Upload `KettyBot.sysml` (or paste its content).
4. Open the project → open the **KettyBot General View** to see the system
   decomposition diagram, and explore the requirements / use cases.

> **Note:** KettyBot and PUDU are trademarks of their respective owners. This
> model is an independent educational/demonstration work, not affiliated with or
> endorsed by Pudu Robotics.
