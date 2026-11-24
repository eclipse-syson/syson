// Covers an unnamed feature with a leading multiplicity before redefinition then subsetting (SpecializationClausePlanner)
package UnnamedMultiplicityRedefinitionSubsetting {
    part a;
    part b;
    part c {
        part [1] :>> a :> b;
        part [1] ordered :>> a :> b;
        part :>> a :> b [1];
        part [1] :> b :>> a;
    }
}
