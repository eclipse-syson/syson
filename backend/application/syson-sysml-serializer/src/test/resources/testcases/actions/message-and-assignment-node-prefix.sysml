// Covers occurrence usage prefixes and usage declarations of accept, send and assignment nodes (MessageActionRenderer, AssignmentActionRenderer)
package MessageAndAssignmentNodePrefix {
    item def E;
    action def A;
    metadata def K;
    action def Run {
        attribute v;
        port p;
        individual accept E;
        abstract action s send E via p;
        #K action <r1> r : A accept e : E;
        ref send E;
        snapshot action w : A assign v := 1;
    }
}
