// Covers allocation declaration subsetting (ConnectorRenderer)
package AllocationDeclarationSpecializations {
    part a;
    part b;
    allocation x;
    allocation al :> x allocate a to b;
}
