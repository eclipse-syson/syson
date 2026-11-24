action def A1 {
    private import ScalarValues::Integer;
    attribute x : Integer;
    action a0;
    action a1;
    action a2;
    action a3;
    then decide d1;
        succession sd1 first d1 if x < 0 then a1;
        succession sd2 first d1 if x == 0 then a2;
        succession sd3 first d1 then a3;
}
