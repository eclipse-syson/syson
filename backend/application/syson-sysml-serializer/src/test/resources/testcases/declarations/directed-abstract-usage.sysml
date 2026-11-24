package DirectedAbstractUsage {
    doc /* Covers the grammar-defined direction-before-abstract prefix order. */

    item def Data;
    action def Processing {
        in abstract item input defined by Data [0..*];
        out abstract item output defined by Data [0..*];
    }
}
