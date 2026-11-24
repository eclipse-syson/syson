// Covers succession target multiplicity before an action with the same name (SuccessionRenderer)
package SuccessionTargetMultiplicity {
    action def Run {
        action a;
        first a then [2] b;
        action b[*];
    }
}
