package FlowPayloadBranches {
    item def Payload;
    part source;
    part destination;
    flow valueOnly of = 1 from source to destination;
    flow specialized of packet : Payload [1] : Payload from source to destination;
    flow multiplicityFirst of [1] : Payload : Payload from source to destination;
}
