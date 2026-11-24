// Covers nonunique next to ordered before a subsetting (SpecializationRenderer#writeStandardOrderedAndSubsetting)
package NonuniqueWithOrderedBeforeSubsetting {
    part def T;
    part a;
    ref part x : T [0..*] nonunique ordered :> a;
    part y : T [1] ordered nonunique :> a;
}
