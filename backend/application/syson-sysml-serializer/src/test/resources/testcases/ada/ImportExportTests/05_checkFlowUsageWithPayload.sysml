package 'Port Example' {
    attribute def Temp;
    part def Fuel;
    port def FuelOutPort {
        attribute temperature : Temp;
        out item fuelSupply : Fuel;
        in item fuelReturn : Fuel;
    }
    port def FuelInPort {
        attribute temperature : Temp;
        in item fuelSupply : Fuel;
        out item fuelReturn : Fuel;
    }
    part def FuelTankAssembly {
        port fuelTankPort : FuelOutPort;
    }
    part def Engine {
        port engineFuelPort : FuelInPort;
    }
}
package 'Flow Connection Interface Example' {
    private import 'Port Example'::*;
    part def Vehicle;
    part vehicle : Vehicle {
        part tankAssy : FuelTankAssembly;
        part eng : Engine;
        flow of Fuel from tankAssy.fuelTankPort.fuelSupply to eng.engineFuelPort.fuelSupply;
        flow of Fuel from eng.engineFuelPort.fuelReturn to tankAssy.fuelTankPort.fuelReturn;
        flow of [1] Fuel from eng.engineFuelPort.fuelReturn to tankAssy.fuelTankPort.fuelReturn;
        flow of Fuel [1] from eng.engineFuelPort.fuelReturn to tankAssy.fuelTankPort.fuelReturn;
        flow of fuel : Fuel from eng.engineFuelPort.fuelReturn to tankAssy.fuelTankPort.fuelReturn;
        flow of fuel [1] : Fuel from eng.engineFuelPort.fuelReturn to tankAssy.fuelTankPort.fuelReturn;
        flow of Fuel from eng.engineFuelPort.fuelReturn to tankAssy.fuelTankPort.fuelReturn;
    }
}
