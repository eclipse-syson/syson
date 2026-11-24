// Covers performed action bodies of state subactions and transition effects (CompactActionRenderer, TransitionRenderer)
package StateSubactionPerformBody {
    attribute def V;
    action def C {
        in x : V;
        out y : V;
    }
    part def B {
        action c : C;
    }
    part b : B;
    attribute z : V;
    state def S {
        entry b.c {
            in x : V :> z = 1;
            out y[1] ordered;
        }
        do action d : C {
            in x : V;
            action n : C;
        }
        exit action e {
            action n;
            first start;
            then n;
        }
        entry action {
            first start;
            then action m;
        }
        state s1;
        state s2;
        transition t1 first s1 do b.c then s2;
        transition t2 first s1 do b.c {
            in x : V;
        } then s2;
        transition t3 first s1 do action f : C {
            in x : V;
        } then s2;
    }
}
