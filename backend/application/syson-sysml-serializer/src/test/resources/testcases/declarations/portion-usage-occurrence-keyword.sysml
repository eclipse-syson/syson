// Covers the optional occurrence keyword of snapshot and timeslice usages
// (found with the validation cases checkFeaturePortionSpecialization-conforming and validateOccurrenceUsageIsPortion-conforming)
package PortionUsageOccurrenceKeyword {
    occurrence def Session {
        snapshot occurrence captured : Session;
        timeslice occurrence phase : Session;
    }
}
