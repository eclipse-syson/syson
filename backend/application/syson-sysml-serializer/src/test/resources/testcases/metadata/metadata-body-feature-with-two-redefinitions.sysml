// Covers an unnamed feature of a metadata body that redefines two features
// (found with the validation case validateMetadataFeatureBody-trigger-in-case-of-multiple-redefinitions)
package MetadataBodyFeatureWithTwoRedefinitions {
    metadata def Classification {
        attribute level : ScalarValues::Integer;
        attribute limit : ScalarValues::Integer;
    }
    part def Sample;
    metadata tag : Classification about Sample {
        :>> level :>> limit;
    }
}
