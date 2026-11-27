// Covers the direction and feature prefixes written after the end keyword of connection definition ends
// (found with the validation cases validateFeatureEndNoDirection-trigger-in-case-of-in-end,
// validateFeatureEndIsConstant-conforming and validateFeatureEndNotDerivedAbstractCompositeOrPortion-trigger-in-case-of-*)
package EndFeaturePrefixes {
    part def Vehicle;
    connection def Link {
        end in source : Vehicle;
        end out target : Vehicle;
        end inout both : Vehicle;
        end constant fixed : Vehicle;
        end abstract general : Vehicle;
        end derived computed : Vehicle;
    }
}
