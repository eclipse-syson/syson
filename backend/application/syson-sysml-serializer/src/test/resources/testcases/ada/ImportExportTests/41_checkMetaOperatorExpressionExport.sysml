private import Metaobjects::SemanticMetadata;
part p;
metadata def MD :> SemanticMetadata {
    :>> baseType = p meta SysML::Systems::PartUsage;
}
metadata def MD2 :> SemanticMetadata {
    :>> baseType default p meta SysML::Systems::PartUsage;
}
