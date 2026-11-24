// Extracted from ImportSysMLModelTest#checkTransitionUsageWithAcceptActionUsageTest
attribute def StartSignal;

state myState {
    state off;
    accept StartSignal then on;
    state on;
}
