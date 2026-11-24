// Covers a payload redefinition in the body of an action named send (PayloadReferenceRenderer)
package ActionPayloadRedefinition {
    item def T;
    action def A {
        action send {
            ref :>> payload : T;
        }
    }
    part x {
        ref :>> payload : T;
    }
}
