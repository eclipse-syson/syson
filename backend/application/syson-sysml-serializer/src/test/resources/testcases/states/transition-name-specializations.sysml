// Covers named transition specializations (TransitionRenderer)
package TransitionNameSpecializations {
    state def S {
        transition u first s1 then s2;
        state s1;
        state s2;
        transition t :> u first s1 then s2;
    }
}
