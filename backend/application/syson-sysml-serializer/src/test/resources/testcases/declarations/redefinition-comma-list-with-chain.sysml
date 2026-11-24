// Covers a redefinition comma list whose first target is a feature chain (SpecializationClauseRenderer#mergesRedefinitionGroups)
package RedefinitionCommaListWithChain {
    part def T;
    part def Base {
        part c;
        part d {
            part e;
        }
    }
    part def Derived :> Base {
        part x : T :>> d.e, c;
    }
}
