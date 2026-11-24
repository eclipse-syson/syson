// Covers the action declaration of named accept and assignment subactions and effects (CompactActionRenderer, TransitionRenderer)
package NamedAcceptAndAssignSubactions {
    item def E;
    state def S {
        attribute v;
        entry action n assign v := 1;
        do action m accept E;
        exit action accept E;
        state s1;
        state s2;
        transition t first s1 do action k assign v := 2 then s2;
    }
}
