// Extracted from ImportSysMLModelTest#checkTransitionWithImplicitSource
   action def A1 {
    private import ScalarValues::Integer;
    attribute x : Integer;
    action a1;
    action a2;
    action a3;
    decide d;
        if x == 1 then a1;
        if 2 > x then a2;
        else a3;
}
