// Covers accept payload multiplicity, subsetting and trigger value (MessageActionRenderer)
package AcceptPayloadParts {
    attribute def Integer;
    action def Run {
        port p;
        ref q;
        accept s1 : Integer [2] via p;
        accept s2 :> q;
        accept s3 : Integer at s1;
    }
    state def TransitionTrigger {
        port p;
        state s1;
        state s2;
        transition first s1 accept sig : Integer [2] via p then s2;
    }
    state def TargetTransition {
        state s1;
        accept x : Integer [2] then s1;
    }
}
