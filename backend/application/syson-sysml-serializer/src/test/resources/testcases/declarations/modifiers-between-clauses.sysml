// Covers ordered and nonunique between clauses without multiplicity (SpecializationClausePlanner#modifierSlot)
package ModifiersBetweenClauses {
    part def T;
    part def Base {
        part a;
        part b;
    }
    part def Derived :> Base {
        part x :> a ordered :>> b;
        part y : T nonunique :> a :>> b;
        part z :> a :> b ordered;
    }
}
