// Extracted from ImportSysMLModelTest#checkSemanticMetadataDefinition
private import Metaobjects::SemanticMetadata;

part def Functions {
    attribute x : ScalarValues::String;
}

part functions : Functions [*] nonunique;

metadata def Function :> SemanticMetadata {
    :>> baseType = functions meta SysML::ActionUsage;
}

#Function action a0;
action a1;
metadata Function about a1;
#Function action def A0;
