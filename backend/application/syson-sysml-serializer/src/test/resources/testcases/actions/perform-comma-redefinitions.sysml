package CommaRedefinitions {
    action def Base {
        action firstAction;
        action secondAction;
    }
    action def Derived :> Base {
        perform firstAction :>> Base::firstAction, Base::secondAction;
    }
}
