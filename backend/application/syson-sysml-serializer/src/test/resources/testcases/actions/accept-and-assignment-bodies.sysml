// Covers body members of accept, send and assignment actions in statements, state subactions and effects (MessageActionRenderer, AssignmentActionRenderer)
package AcceptAndAssignmentBodies {
    attribute def I;
    item def E;
    action def Run {
        attribute v;
        port p;
        accept E via p {
            in x : I;
        }
        assign v := 1 {
            in y : I;
        }
    }
    state def S {
        attribute v;
        entry accept E {
            in x : I;
        }
        do assign v := 1 {
            in y : I;
        }
        exit send E {
            in z : I;
        }
        state s1;
        state s2;
        transition t1 first s1 do accept E {
            in x : I;
        } then s2;
        transition t2 first s1 do assign v := 2 {
            in y : I;
        } then s2;
    }
}
