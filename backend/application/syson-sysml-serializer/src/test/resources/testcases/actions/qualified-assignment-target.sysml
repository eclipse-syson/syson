package QualifiedAssignmentTarget {
    doc /* Covers an assignment target qualified by a receiver feature. */

    action writePayload {
        out item payload;
        assign payload.diagnosticsPacket := "written";
    }
}
