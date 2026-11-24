package SpecializationOrder {
    doc /* Covers subsetting before redefinition, with and without an intervening multiplicity. */

    item subsetTarget;
    item redefinitionTarget;
    item orderedItem subsets subsetTarget redefines redefinitionTarget;
    item multiplicityItem subsets subsetTarget [0..*] redefines redefinitionTarget;
}
