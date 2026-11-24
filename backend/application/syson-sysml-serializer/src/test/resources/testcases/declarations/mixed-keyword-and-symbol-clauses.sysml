// Covers keyword and symbol spellings mixed among clauses of one kind (SpecializationClauseRenderer#operatorAsWritten)
package MixedKeywordAndSymbolClauses {
    part def Base {
        part a;
        part b;
        part c;
    }
    part def Derived :> Base {
        part x subsets a :> c;
        part y redefines a :>> b;
        part z :> a subsets c;
    }
}
