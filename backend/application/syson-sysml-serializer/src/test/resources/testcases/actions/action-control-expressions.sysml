package ActionControlExpressions {
    action def Run {
        attribute count = 0;
        if (count + 1) > 0 {
            assign count := count + 1;
        } else {
            assign count := 0;
        }
        while count < 3 {
            assign count := count + 1;
        } until count == 3;
        for index in (1, 2, 3) {
            assign count := count + index;
        }
    }
}
