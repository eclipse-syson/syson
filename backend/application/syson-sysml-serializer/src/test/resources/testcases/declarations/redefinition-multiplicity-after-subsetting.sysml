// Covers a trailing multiplicity after redefinition then subsetting (SpecializationRenderer#redefinitionLayoutMultiplicity)
package RedefinitionMultiplicityAfterSubsetting {
    part def Base {
        part a;
        part b;
    }
    part def Derived :> Base {
        part x :>> a :> b [1];
        occurrence y :>> a :> b [1];
    }
}
