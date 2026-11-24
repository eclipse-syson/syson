action def A1 {
    private import ScalarValues::Integer;
    attribute x : Integer;
    action a1;
    action a2;
    first a1 if x == 1 then a2;
    succession s1 first a1 if x > 1 & x < 2 then a2;
    succession s2 first a1 if x > 2 & x < 3 then a2 {
        doc /* Some documentation on that succession */
    }
}
