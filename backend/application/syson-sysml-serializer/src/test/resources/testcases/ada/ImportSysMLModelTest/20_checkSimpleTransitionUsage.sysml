// Extracted from ImportSysMLModelTest#checkSimpleTransitionUsage
action def A1 {
    private import ScalarValues::Integer;
    action a1 ;
    action a2 ;
    attribute x : Integer;
    succession S first a1 if x == 1 then a2;
}
