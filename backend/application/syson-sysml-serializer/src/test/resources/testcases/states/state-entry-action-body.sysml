state def Modes {
    attribute faults : Integer = 0;

    state safing {
        entry action record {
            assign faults := faults + 1;
        }
    }
}
