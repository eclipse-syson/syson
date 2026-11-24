// Covers ordered right after a leading multiplicity (StandardSpecializationPlan#renderOrderedBeforeTyping, StandardSpecializationPlanner#orderedPlacement)
package OrderedAfterLeadingMultiplicity {
    part def T;
    part def Base {
        part a;
    }
    part def Derived :> Base {
        part x [1] ordered : T :> a;
        part y [1] ordered : T :>> a;
        part z [1] ordered :>> a;
    }
}
