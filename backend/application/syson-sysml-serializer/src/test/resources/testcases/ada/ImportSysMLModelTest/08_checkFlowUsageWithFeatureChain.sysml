// Extracted from ImportSysMLModelTest#checkFlowUsageWithFeatureChain
part def P1Def {
    port po1 : PortDef1;
}
port def PortDef1 {
    out item item1: P2Def;
}
part def P2Def;
part def P3Def {
    in item item2 : P3Def;
}
part p1 {
    part p2 : P1Def;
    part p3 : P3Def;
    flow from p2.po1.item1 to p3.item2;
}
