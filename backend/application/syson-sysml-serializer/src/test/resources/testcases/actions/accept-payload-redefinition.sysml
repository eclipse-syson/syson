package PayloadRedefinition {
    item def Signal;
    action def Receive {
        accept signal : Signal :>> Actions::AcceptAction::payload;
    }
}
