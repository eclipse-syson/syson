// Covers connection declaration subsetting and short name (ConnectorRenderer)
package ConnectionDeclarationSpecializations {
    part a;
    part b;
    connection x;
    connection cc :> x connect a to b;
    connection <s> cs connect a to b;
}
