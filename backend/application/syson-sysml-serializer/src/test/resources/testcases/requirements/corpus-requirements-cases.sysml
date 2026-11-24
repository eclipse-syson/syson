analysis engineTradeOffAnalysis : TradeStudy {
	part vehicle_b_engine6cyl :> vehicleAlternatives {
		part engine redefines engine {
			attribute fuelEfficiency redefines fuelEfficiency = .5;
		}
	}
}
verification massTests : MassTest {
	then action evaluatePassFail {
		out verdict = PassIf(vehicleSpecification.vehicleMassRequirement(vehicle_uut));
	}
}
use case def TransportPassenger {
	objective TransportObjective {
		require transportRequirements;
	}
}
use case def GetInVehicle {
	assert constraint {
		driver != null xor passenger != null
	}
}
use case transportPassenger_1 : TransportPassenger {
	join join3;
	action trigger accept ignitionCmd : IgnitionCmd;
	fork fork2;
	then driveVehicleToDestination;
	then providePower;
	first join3 then done;
}
concern def VehicleSafety {
	stakeholder se : SafetyEngineer;
}
requirement def OrderedRequirement :> Types::Base::Element, Types::Other {
	attribute values : Types::Value[0..*] ordered;
}
analysis def FuelEconomyAnalysis {
	action dynamicsAnalysis {
		assert constraint straightLineDynamics {
			(1 .. sc.n - 1)->forAll{ in i : Integer; StraightLineDynamicsEquations(p=power#(i), m=vehicle.mass, dt=nextSample.t - thisSample.t, x_i=thisSample.s.position, v_i=thisSample.s.velocity, x_f=nextSample.s.position, v_f=nextSample.s.velocity, a=acceleration#(i)) }
		}
	}
}
#goal requirement deliverPayload {
	assume #goal constraint payloadMassLimit;
}
analysis def AnalysisCase {
	v.m
}
use case 'provide transportation' : 'Provide Transportation' {
	then use case 'drive vehicle' {
		include 'add fuel'[0..*] {
		}
	}
}
analysis engineTradeStudy : TradeStudy {
	subject : Engine[1..*] = all engineChoice;
}
analysis Apollo11MissionDeltaVBudgetAnalysis {
	loiMargin = dv_csm - (dv_required_loi + dv_required_tei);
}
