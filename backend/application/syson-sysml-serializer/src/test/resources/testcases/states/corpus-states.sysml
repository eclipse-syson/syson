state def VehicleStates;
state def S {
	entry ;
	state S1;
	accept s : Sig do action D then S2;
	transition first S1 accept s : Sig do action D then S2.S3;
	transition T first S2.S3 accept s : Sig via p if true do send s via this to p then S1;
	exit act;
	transition first S3.S3a then S1;
}
state def ReindeerController {
	state idle;
	accept StartMission via hmi if reindeerCount == 9 and totalPower >= 36 [SI::kW] do send new Command("Gee-up!") via eyelet then mission;
}
state def Counting {
	part counter : Counter;
	entry assign counter.count := 0;
	then state wait;
	accept Incr then increment;
	accept Decr then decrement;
	state decrement {
	}
	then wait;
}
state 'controller states' : 'Controller States' parallel {
	state 'operational controller states' {
		transition initial then off;
	}
}
state def S {
	transition T first S2.S3 accept s : Sig via p if true do send s via this to p then S1;
}
state def LunarOrbitInsertionPhase :> Phase {
	do action loiPhaseOperations {first start; then action executeLOIBurn: ExecuteLOIBurn; then done;}
}
state def ReindeerController {
	state mission parallel {
		state monitoringReindeer {
			transition low2nomnal first lowEnergy accept when averageEnergy >= 50 then nominal;
		}
	}
	accept EndMission via hmi then idle;
}
