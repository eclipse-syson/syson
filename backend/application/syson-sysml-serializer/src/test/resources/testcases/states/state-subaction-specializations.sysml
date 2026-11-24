// Covers state subaction perform declaration specializations (CompactActionRenderer)
package StateSubactionSpecializations {
    action def P;
    state def S {
        action q;
        entry action p :> q;
        do action r : P {
            action n;
        }
    }
}
