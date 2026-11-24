// Covers ordered kept with a multiplicity that follows a subsetting (StandardSpecializationPlanner#orderedPlacement)
package OrderedAfterSubsettingMultiplicity {
    part def T;
    part a;
    part x : T :> a [1] ordered;
}
