// Covers a feature chain repeating the same quoted name (SysMLNameFormatter#referenceName)
package RepeatedQuotedChainSegment {
    part def A {
        part 'a' : A;
    }
    part 'a' : A;
    part x :> 'a'.'a';
}
