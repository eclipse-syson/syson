package TMS_DataDictionary_Example {
    private import ISQ::*;

    // TODO check connection/difference with AER concepts,
    // TODO now is mainly used as exchange items container

    // Signal
     abstract item def Signal {
        doc /*Generic signal definition*/
        attribute signalVariable: ScalarValues::Real;
    }
    item def CommandTemperature:> Signal {
        doc /*Command setting the temperature of different systems*/
        :>> signalVariable:> temperature;
    }
    item def AirPressure:> Signal {
        doc /*Signal of air pressure from different sources*/
        :>> signalVariable:> pressure;
    }
    item def AirTemperature:> Signal {
        doc /*Signal of air temperature from different sources*/
        :>> signalVariable:> temperature;
    }
    item def Temperature:> Signal {
        doc /*Temperature sensor signal*/
        :>> signalVariable:> temperature;
    }
    
    // TODO check connection/difference with MBD similar concept
    // Flow of matter (or energy)
    abstract item def Flow {
        doc /*Generic flow definition*/
        // Taken from Modelica connector variables definition
        attribute potentialVariable: ScalarValues::Real; // e.g. for electric flow: voltage
        attribute flowVariable: ScalarValues::Real; // e.g. for electric flow: current
    }
    item def AirFlow:> Flow {
        :>> potentialVariable:> pressure;
        :>> flowVariable:> massFlow;
    }
    item def CoolingFlow:> Flow {
        :>> potentialVariable:> pressure;
        :>> flowVariable:> massFlow;
    }
    item def ElectricCurrent:> Flow {
        :>> potentialVariable:> voltage;
        :>> flowVariable:> electricCurrent;
    }
}