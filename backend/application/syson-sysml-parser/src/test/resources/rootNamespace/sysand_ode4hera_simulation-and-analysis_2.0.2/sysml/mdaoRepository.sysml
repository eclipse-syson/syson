package MDAORepository {
    doc /* The reusable MDAO repository */
    
    private import ArchitectureDesign::*;
    private import MultidisciplinaryDesignAnalysisOptimization::*;

    private import ArchitectureFileExample::*;

    part def OADDiscipline :> DisciplineDefinition {
        attribute :>> disciplineName = "OverallAircraftDesign";   // could be used for RCE tool name identification, can also be 'tool name'

        in designRange :> ISQ::distance;
        out mtow :> ISQ::mass;
        out fuelBurn :> ISQ::mass;
        out liftToDrag : ScalarValues::Real;

        // Binding to the architecture
        part aircraft : Aircraft;

        bind designRange = aircraft.designRange;
        bind mtow = aircraft.mtow;
        bind fuelBurn = aircraft.fuelBurn;
        bind liftToDrag = aircraft.liftToDrag;
    }

    part def PropulsionDiscipline :> DisciplineDefinition {
        attribute :>> disciplineName = "Propulsion";

        in designRange :> ISQ::distance;
        out thrust :> ISQ::force;
        out fuelBurn :> ISQ::mass;
        out bpr : ScalarValues::Real;

        
        part aircraft : Aircraft;
    
        bind designRange = aircraft.designRange;
        bind fuelBurn = aircraft.fuelBurn;
        bind bpr = aircraft.engine.bpr;
        bind thrust = aircraft.engine.thrust;
    }

    part def AerodynamicsDiscipline :> DisciplineDefinition {
        attribute :>> disciplineName = "Aerodynamics";

        in designRange :> ISQ::distance;
        in aspectRatio : ScalarValues::Real;
        in span :> ISQ::length;
        out liftToDrag : ScalarValues::Real;
        out takeoffFieldLength :> ISQ::length;

        
        part aircraft : Aircraft;
        part wing : Wing;                                       // Example of discipline with more than one architectural component

        bind aspectRatio = wing.aspectRatio;
        bind span = wing.span;

        bind designRange = aircraft.designRange;
        bind liftToDrag = aircraft.liftToDrag;
        bind takeoffFieldLength = aircraft.takeoffFieldLength;
    }

    part def WeightDiscipline :> DisciplineDefinition {
        attribute :>> disciplineName = "Weight";

        in liftToDrag : ScalarValues::Real;
        out mtow :> ISQ::mass;

        part aircraft : Aircraft;

        bind liftToDrag = aircraft.liftToDrag;
        bind mtow = aircraft.mtow;
    }

    part def PropellerDiscipline :> DisciplineDefinition {
        attribute :>> disciplineName = "PropellerPerformance";

        in diameter :> ISQ::diameter;
        in efficiency : ScalarValues::Real;
        out takeoffFieldLength :> ISQ::length;


        part aircraft : Aircraft;
        part propeller : Propeller;

        bind diameter = propeller.diameter;
        bind efficiency = propeller.efficiency;
        bind takeoffFieldLength = aircraft.takeoffFieldLength;
    }

}