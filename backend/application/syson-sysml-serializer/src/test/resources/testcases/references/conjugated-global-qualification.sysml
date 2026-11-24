// Covers the global qualification after a conjugation (NameEscaper#escapeCompoundReferenceName)
package ConjugatedGlobalQualification {
    port def P;
    package Q {
        port def P;
    }
    part x {
        port p : ~$::P;
        port q : ~$::Q::P;
    }
}
