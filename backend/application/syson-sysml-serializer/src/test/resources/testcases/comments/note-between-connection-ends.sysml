// Covers a note ending the line between the two ends of a connection usage
// (found with the validation case checkFeatureEndRedefinition-trigger-in-case-of-usage-subsetting-itself)
package NoteBetweenConnectionEnds {
    part entrance;
    part exitPort;
    connection link connect entrance // note after the source end
        // second source note
        to exitPort;
    allocate entrance // allocation source
        to exitPort;
}
