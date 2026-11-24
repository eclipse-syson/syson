// Covers exhibit state shorthand with parallel body (ExhibitStateRenderer)
package ExhibitStateParallel {
    part def S {
        state u;
    }
    part s : S;
    part e {
        exhibit s.u parallel {
        }
    }
}
