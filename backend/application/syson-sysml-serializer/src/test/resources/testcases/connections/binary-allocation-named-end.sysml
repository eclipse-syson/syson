// Covers the binary allocate form with a named end (ConnectorRenderer)
package BinaryAllocationNamedEnd {
    part def System {
        part logical;
        part physical;
        allocate source ::> logical to physical;
        allocate (source ::> logical, target ::> physical);
    }
}
