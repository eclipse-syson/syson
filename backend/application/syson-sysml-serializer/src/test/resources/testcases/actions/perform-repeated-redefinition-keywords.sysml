package PerformedRedefinitions {
    action def Base {
        action firstAction;
        action secondAction;
    }
    action def Derived :> Base {
        perform firstAction redefines firstAction redefines secondAction;
    }
}
