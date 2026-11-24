// Covers accept payload value parts written with their feature value operator (MessageActionRenderer)
package AcceptPayloadValue {
    attribute def S;
    action def Run {
        attribute v : S;
        accept = v;
        action named accept x = v;
        accept y : S := v;
        accept z default = v;
        accept at v;
    }
    state def TransitionTrigger {
        attribute v : S;
        state s1;
        state s2;
        transition first s1 accept x = v then s2;
        transition first s1 accept y : S = v then s2;
    }
}
