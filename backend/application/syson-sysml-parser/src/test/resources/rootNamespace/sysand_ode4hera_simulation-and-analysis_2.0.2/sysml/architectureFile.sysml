package ArchitectureFileExample {

    private import ArchitectureDesign::*;

    // Reusable architecture part definitions

    part def Engine {
        attribute bpr : ScalarValues::Real;
        attribute thrust :> ISQ::force;
    }

    part def Wing {
        attribute area :> ISQ::area;
        attribute aspectRatio : ScalarValues::Real;
        attribute span :> ISQ::length;
    }

    part def Propeller {
        attribute diameter :> ISQ::diameter;
        attribute efficiency : ScalarValues::Real;
    }

    abstract part def Aircraft {
        attribute designRange :> ISQ::distance;
        attribute mtow :> ISQ::mass;
        attribute fuelBurn :> ISQ::mass;
        attribute liftToDrag : ScalarValues::Real;
        attribute takeoffFieldLength :> ISQ::length;
        part engine : Engine;
    }

    part def TubeWingAircraft :> Aircraft {
        part wing : Wing;
        
    }

    part def BlendedWingBodyAircraft :> Aircraft {
        part centerBodyWing : Wing;
    }

    part def TurbopropAircraft :> Aircraft {
        part wing : Wing;
        part propeller : Propeller;
    }
        

    // Architecture Design Space

    abstract #architectureDesignSpace part archDesignSpace {

        action transportPax :> boundaryFunctions;

        #subsystem part longRangeTubeWingSpace : TubeWingAircraft {
            // Defining this design variable as abstract means that it
            // will not be realized at the SAO level, but rather it will
            // remain a design variable at the architecture-instance-level
            abstract #contX attribute :>> designRange {
                :>> lowerBound = 12000[SI::km];
                :>> upperBound = 16000[SI::km];
            }

            #minObj attribute :>> mtow;
            #evalOut attribute :>> fuelBurn;
            #evalOut attribute :>> liftToDrag;

            part fuselage {
                perform transportPax;
                #needsAction references generateLift;
                #needsAction ::> generateThrust;
            }

            action generateLift;
            action generateThrust;

            part :>> wing {
                perform generateLift;
                attribute :>> area = 845[SI::'metre squared'];
                attribute :>> aspectRatio = 9.4;
                attribute :>> span = 79.8[SI::m];
            }

            part :>> engine {
                perform generateThrust;
                attribute :>> bpr = 10.5;
                attribute :>> thrust = 320000[SI::N];
            }
        }


        #subsystem part bwbSpace : BlendedWingBodyAircraft {
            abstract #contX attribute :>> designRange {
                :>> lowerBound = 10000[SI::km];
                :>> upperBound = 15000[SI::km];
            }

            #minObj attribute :>> fuelBurn;
            #evalOut attribute :>> mtow;
            #evalOut attribute :>> liftToDrag;

            part :>> centerBodyWing {
                perform transportPax;
                #needsAction ::> generateThrust;

                attribute :>> area = 920[SI::'metre squared'];
                attribute :>> aspectRatio = 7.8;
                attribute :>> span = 84.0[SI::m];
            }

            action generateThrust;

            part :>> engine[2] {
                perform generateThrust;
                attribute :>> bpr default 12.0;
                attribute :>> thrust default 280000[SI::N];
            }
        }


        #subsystem part regionalTurbopropSpace : TurbopropAircraft {
            abstract #contX attribute :>> designRange {
                :>> lowerBound = 1000[SI::km];
                :>> upperBound = 2500[SI::km];
            }

            #minObj attribute :>> takeoffFieldLength;
            #evalOut attribute :>> mtow;
            #evalOut attribute :>> fuelBurn;

            part fuselage {
                perform transportPax;
                #needsAction ::> generateLift;
                #needsAction ::> generateThrust;
            }

            action generateLift;
            action generateThrust;

            part :>> wing {
                perform generateLift;

                attribute :>> area = 70[SI::'metre squared'];
                attribute :>> aspectRatio = 11.5;
                attribute :>> span = 28.4[SI::m];
            }

            part :>> propeller {
                perform generateThrust;
                #needsAction ::> generateMechanicalPower;

                attribute :>> diameter = 4.1[SI::m];
                attribute :>> efficiency = 0.86;
            }

            action generateMechanicalPower;

            part :>> engine {
                perform generateMechanicalPower;

                attribute :>> bpr = 0.0;
                attribute :>> thrust = 45000[SI::N];
            }
        }

    }

    // Architecture Instances

    #architecture part longRangeTubeWingNominal subsets archDesignSpace {

        part :>> longRangeTubeWingSpace {

            attribute :>> designRange;
            attribute :>> mtow;
            attribute :>> fuelBurn;
            attribute :>> liftToDrag;
            attribute :>> takeoffFieldLength;
        }

        part :>> bwbSpace[0];
        part :>> regionalTurbopropSpace[0];
    }

    #architecture part bwbNominal subsets archDesignSpace {

        part :>> bwbSpace {

            attribute :>> designRange;
            attribute :>> mtow;
            attribute :>> fuelBurn;
            attribute :>> liftToDrag;
            attribute :>> takeoffFieldLength;

            part :>> engine[2] = (engine_1, engine_2);
            
            part engine_1 :> engine {
                attribute :>> bpr = 12.0;
                attribute :>> thrust = 280000[SI::N];
            }

            part engine_2 :> engine {
                attribute :>> bpr = 12.0;
                attribute :>> thrust = 280000[SI::N];
            }
        }

        part :>> longRangeTubeWingSpace[0];
        part :>> regionalTurbopropSpace[0];
    }

    #architecture part regionalTurbopropNominal subsets archDesignSpace {

        part :>> regionalTurbopropSpace {
            attribute :>> designRange;
            attribute :>> mtow;
            attribute :>> fuelBurn;
            attribute :>> liftToDrag;
            attribute :>> takeoffFieldLength;

            part :>> wing {
                attribute :>> area;
                attribute :>> aspectRatio;
                attribute :>> span;
            }

            part :>> propeller {
                attribute :>> diameter;
                attribute :>> efficiency;
            }

            part :>> engine {
                attribute :>> bpr;
                attribute :>> thrust;
            }
        }

        part :>> longRangeTubeWingSpace[0];
        part :>> bwbSpace[0];
    }
}