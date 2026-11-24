// Covers exhibit state feature chain target (PerformedActionTargetResolver)
package ExhibitStateFeatureChain {
    part def T {
        state u;
    }
    part def S {
        part t : T;
    }
    part s : S;
    part e {
        exhibit s.t.u;
    }
}
