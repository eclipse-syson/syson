// Covers an end feature declared in the body of a binding connector usage, after its two bound features
// (found with the validation case validateBindingConnectorIsBinary-trigger-in-case-of-3-ends)
package BindingConnectorBodyEnd {
    part def Device {
        part left;
        part right;
        part extra;
        binding equality bind left = right {
            end ref ::> extra;
        }
    }
}
