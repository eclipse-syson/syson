action def A1 {
    action a1;
    action a2;
    action a3;
    attribute x : ScalarValues::Real;
    decide decision1;
    if x >= 2.1 then a1;
    if x >= 1.1 and x < 2.1 then a2;
    else a3;
}
