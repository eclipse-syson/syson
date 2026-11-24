// Extracted from ImportSysMLModelTest#checkTransitionUsageWithAcceptActionUsageAndSendSignalActionTest
attribute def StartSignal;

state myState {
    state off;
    accept StartSignal
        do send StartSignal()
        then on;
    state on;
}
