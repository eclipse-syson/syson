action def AcceptMessageAction :> Action, AcceptPerformance {
	doc /* Note. */
	inout :>> payload;
	ref acceptedMessage :>> acceptedTransfer : MessageTransfer, MessageAction {
		in :>> MessageTransfer::payload, MessageAction::payload;
	}
}
action def WhileLoopAction :> LoopAction, LoopPerformance {
	in untilTest default { false } {
	}
}
action def ForLoopAction :> LoopAction {
	private action initialization assign index := 1;
}
action a1 {
	then merge m;
	then accept sig after 10 [SI::s];
	then accept at new Time::Iso8601DateTime("2022-01-30T01:00:00Z");
	then send new S() to b;
	then accept when b.f;
	then decide;
	if true then m;
	else done;
}
action a2 {
	flow aa.target to snd.receiver;
	action snd send {
	}
	action snd2 send via this to aa.target;
	bind s = snd2.payload;
}
action def c {
	then action c1 {
		terminate c1;
	}
}
action 'Root Function' : Function {
	action 'read movement' : FunctionalChain {
		first start;
		then 'Root Function'.'Capture Movement';
		then 'Root Function'.'Transform Raw Optical Data';
		then done;
	}
}
action def A1 {
	private import ScalarValues::Integer;
	then decide d1;
	succession sd1 first d1 if x < 0 then a1;
	succession sd3 first d1 then a3;
}
action {
	if i < 0 {
	}
	else if i == 0 {
	}
	else {
	}
	then action aLoop while i > 0 {
	}
	until b;
	then while i > 0 {
	}
	loop {
	}
	until b;
}
action def MonitoredActivity {
	then fork;
	then performCriticalActivity;
	then waitForTimeOut;
	action performCriticalActivity {
		perform criticalActivity;
		then terminate;
	}
	then stop;
	action stop terminate;
}
action counter {
	attribute count default := 0;
}
action def Function :> ArcadiaElement {
	private doc /* Note. */
	alias subFunctions for subactions;
}
action illuminateRegion {
	succession flow lightFlow from produceDirectedLight.light to reflectLight.light;
}
action takePicture : TakePicture {
	first focus if focus.image.isWellFocused then shoot;
}
action def ChargeBattery {
	loop action charging {
		action monitor : MonitorBattery {
		}
	}
	until charging.monitor.charge >= 100;
}
#Function action def A0;
action def ActionDef1 {
	first a0;
	then action a2;
}
