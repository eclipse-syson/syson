package AssignmentTargetBranches {
    part source;
    part destination;
    attribute projected = source->Transform projection;
    action def SetPayload {
        assign source.destination[0].value := projected;
        assign source->Transform projection.value := projected;
        assign source->Transform { source }.value := projected;
    }
}
