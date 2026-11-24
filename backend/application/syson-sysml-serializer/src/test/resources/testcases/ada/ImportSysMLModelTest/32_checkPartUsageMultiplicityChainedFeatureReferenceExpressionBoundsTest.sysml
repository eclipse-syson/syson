// Extracted from ImportSysMLModelTest#checkPartUsageMultiplicityChainedFeatureReferenceExpressionBoundsTest
part bounds {
    attribute lower:ScalarValues::Integer = 1;
    part upperBounds {
        attribute upper:ScalarValues::Integer = 2;
    }
}

part myPart[bounds::lower..bounds::upperBounds::upper];
