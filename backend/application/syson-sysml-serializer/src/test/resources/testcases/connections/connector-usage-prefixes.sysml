// Covers occurrence usage prefixes of connector-like declarations (ConnectorRenderer, InterfaceRenderer, FlowRenderer)
package ConnectorUsagePrefixes {
    part a;
    part b;
    interface def I;
    connection c0 connect a to b;
    abstract connection c1 connect a to b;
    ref connection c2 connect a to b;
    individual connection c3 connect a to b;
    snapshot connection c4 connect a to b;
    in derived constant connection c5 connect a to b;
    variation connection c6 connect a to b;
    abstract connect a to b;
    abstract interface i1 : I connect a to b;
    out ref individual timeslice interface i2 connect a to b;
    abstract interface a to b;
    abstract allocation l1 allocate a to b;
    inout ref allocation l2 allocate a to b;
    abstract allocate a to b;
    ref binding k1 bind a = b;
    in binding k2 bind a = b;
    abstract flow f1 from a to b;
    ref individual flow f2 from a to b;
    ref message m1 from a to b;
}
