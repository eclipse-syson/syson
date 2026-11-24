// Covers state body members of exhibit state shorthand targets (ExhibitStateRenderer)
package ExhibitStateShorthandBody {
    part def P {
        exhibit state s;
    }
    part p : P {
        exhibit s {
            entry;
            then x;
            do action a;
            state x;
        }
    }
}
