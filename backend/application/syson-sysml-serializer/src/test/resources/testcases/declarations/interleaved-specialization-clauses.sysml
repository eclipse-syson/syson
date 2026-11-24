// Covers source interleaving of specialization clauses and multiplicity (SpecializationRenderer layout selection)
package InterleavedSpecializationClauses {
    part def T;
    part def U;
    part def Base {
        part a;
        part b;
    }
    part def Derived :> Base {
        part p1 :> a [1] : T;
        part p2 [1] :> a : T;
        part p3 ::> a [1] : T;
        part p4 :>> b :> a : T;
        part p5 :> a :>> b : T;
        part p6 : T :>> a :> b;
        part p7 ::> a :> b;
        ref r8 ::> a :> b : T;
        part p9 : T :> a : U;
        part p10 :> a : T :> b;
        part p11 :> a [1] :> b : T;
    }
}
