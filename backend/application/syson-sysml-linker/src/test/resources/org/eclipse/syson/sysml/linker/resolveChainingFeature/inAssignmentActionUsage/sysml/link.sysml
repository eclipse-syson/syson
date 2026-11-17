package P {
	
	part def Part1 {
		attribute attrubute1;
	}
	
	state def State1 {
		part part1 : Part1;
	}

	action action1 {
		state state1 : State1;
		assign state1.part1.attrubute1 := 1;
	}
}