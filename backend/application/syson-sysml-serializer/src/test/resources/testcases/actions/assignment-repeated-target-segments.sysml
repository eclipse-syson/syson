package AssignmentRepeatedTargetSegments {
    action update {
        assign level.level.level := 0;
        assign level[1].level := 1;
        assign Storage::level.level := 2;
        assign 'level.level'.'level.level' := 3;
        action named assign level.level := 4 {
            attribute delay;
        }
    }
    state charging {
        entry assign level.level := 0;
        do assign source.level := 1;
        exit assign level := 2;
    }
    state def Charging {
        state idle;
        state full;
        transition first idle do assign level.level := 1 then full;
    }
}
