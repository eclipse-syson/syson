package Assignment3 {
    part def Counter {
		attribute count : ScalarValues::Integer := 0;
    }

	action a {
		part counter : Counter;
		assign a.counter.count := a.counter.count + 1;
	}
}
