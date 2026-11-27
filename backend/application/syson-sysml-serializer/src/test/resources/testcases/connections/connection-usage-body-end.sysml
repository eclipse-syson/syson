// Covers an end feature declared in the body of a connection usage, after its connected ends
// (found while reducing the validation case validateBindingConnectorIsBinary-trigger-in-case-of-3-ends)
package ConnectionUsageBodyEnd {
    part def Device {
        part left;
        part right;
        part extra;
        connection link connect left to right {
            end ref ::> extra;
        }
    }
}
