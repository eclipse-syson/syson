package Conjugated {
    attribute def Temp;
    port def TempPort {
        attribute temperature : Temp;
    }
    part def TempPortClassic {
        port tempPortClassic : TempPort;
    }
    part def TempPortConj {
        port tempPortConj : ~TempPort;
    }

}
