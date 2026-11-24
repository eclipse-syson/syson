// Covers the grouping of a looser classification-test source (OperatorExpressionRenderer#classificationTestText, #metadataTestText)
package ClassificationTestSourceGrouping {
    attribute def T;
    attribute equalityTest = (a == b) istype T;
    attribute disjunctionType = (a or b) hastype T;
    attribute metadataTest = (a or b) @ T;
}
