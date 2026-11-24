// Covers identified and keyword-only state subactions, transition effects and performs, which are not the empty
// entry placeholder (ModelNavigation#isEmptyStateEntryAction, ActionRenderer, ActionNodePrefixRenderer)
package StateSubactionIdentification {
    action def A;
    attribute def Sig;
    part def P {
        ref x : Sig;
        state s0 {
            entry;
        }
        state s1 {
            entry action <e1>;
            do action <d1>;
            exit action <x1>;
        }
        state s2 {
            entry action;
            do action <d2> d2Name;
            exit action { }
        }
        state s3 {
            entry action <e3> : A;
            do action <d3> {
                action inner;
            }
            exit action <x3> : A { }
        }
        state s4 {
            entry action <e4> accept Sig;
            do action <d4> send Sig() to x;
            exit action <x4> assign x := Sig();
        }
        state def T {
            entry; then t1;
            state t1;
            state t2;
            transition first t1 accept Sig do action <ef1> then t2;
            transition first t1 accept Sig do action <ef2> send Sig() to x then t2;
        }
    }
    action def Q {
        perform action <p1>;
        perform action <p2> p2Name : A;
    }
}
