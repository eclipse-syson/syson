// Extracted from ImportSysMLModelTest#checkRedefinedFeatureToInheritedFields
private import ISQBase::mass;
#MP part p2 {
    attribute :>> mass = 2.0;
}
private import Metaobjects::SemanticMetadata;
part p {
    attribute mass;
}
metadata def MP :> SemanticMetadata {
    :>> baseType = p meta SysML::Systems::PartUsage;
    :> annotatedElement : SysML::Systems::PartUsage;
}
part p3 :> p1::p2::p0 {
    attribute :>> mass = 2.0;
}
package p1 {
    package p2 {
        part p0 :> p;
    }
}
part p {
    attribute mass;
}
