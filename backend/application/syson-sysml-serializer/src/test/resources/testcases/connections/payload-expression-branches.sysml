package PayloadExpressionBranches {
    item def Payload;
    part source;
    part destination;
    flow valueOnly of = 1 from source to destination;
    flow specialized of packet : Payload [1] : Payload from source to destination;
    flow multiplicityFirst of [1] : Payload : Payload from source to destination;

    attribute projected = source->Transform projection;
    action def SetPayload {
        assign source.destination[0].value := projected;
        assign source->Transform projection.value := projected;
        assign source->Transform { source }.value := projected;
        if true action {
            send projected;
        }
    }
}
