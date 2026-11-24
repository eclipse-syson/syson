// Covers repeated single-target reference and cross subsetting clauses (SpecializationClauseRenderer#writeReferenceSpecializations)
package RepeatedReferenceAndCrossSubsettings {
    part def T;
    part a;
    part b;
    part x : T ::> a ::> b;
    part y : T => a => b;
    part z : T references a references b;
}
