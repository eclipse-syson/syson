package Assignment1 {
    part def Counter {
		attribute count : ScalarValues::Integer := 0;
		action incr {
			assign count := 1;
		}
    }
}
