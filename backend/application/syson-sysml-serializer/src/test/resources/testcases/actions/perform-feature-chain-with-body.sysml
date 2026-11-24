// Covers performed feature chain targets kept whole when the perform has a body (PerformedActionTargetResolver)
package PerformFeatureChainWithBody {
    action def C {
        in x;
    }
    part def B {
        action c : C;
    }
    part def A {
        part b : B;
    }
    part a : A;
    part p {
        perform a.b.c {
            in x = 1;
        }
    }
    state def S {
        entry a.b.c {in x=1;}
        exhibit a.b.c;
    }
}
