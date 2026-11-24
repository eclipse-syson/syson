private import Metaobjects::SemanticMetadata;
requirement def Goal;
requirement goals : Goal;
metadata def goal :> SemanticMetadata {
    :>> baseType = goals meta SysML::Systems::RequirementUsage;
}
#goal requirement r2 {
    assume #goal constraint c1;
    require #goal constraint c2;
}
