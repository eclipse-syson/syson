// Covers the typing and the subsetting of an owned cross feature declared before an end feature
// (found with the validation cases validateFeatureCrossFeatureType-conforming and
// validateFeatureCrossFeatureType-trigger-in-case-of-owned-cross-type)
package OwnedCrossFeature {
    part def Member;
    item allMembers : Member [0..*];
    connection def Link {
        end derived <cp> crossPart : Member [0..1] :> allMembers item left : Member;
        end right : Member;
    }
}
