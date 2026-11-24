package AssignmentGroupedTargetSource {
    action update {
        assign (storage).level := 0;
        assign (level).level := 1;
        assign (storage.tank).level := 2;
        assign (primary + backup).level := 3;
        assign storage.tanks[1].level := 4;
    }
    state charging {
        entry assign (storage).level := 0;
    }
}
