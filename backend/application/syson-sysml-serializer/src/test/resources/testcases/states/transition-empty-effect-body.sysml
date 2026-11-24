// Covers unnamed perform effect with empty body (TransitionRenderer)
package TransitionEmptyEffectBody {
    state def S {
        state s1;
        state s2;
        transition first s1 do action {
        } then s2;
    }
}
