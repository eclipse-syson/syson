package Profile {
    private import Metaobjects::SemanticMetadata;

    occurrence def Cause {
        attribute occurs;
    }

    abstract occurrence causes : Cause [*] nonunique;

    metadata def <cause> CauseMetadata :> SemanticMetadata {
        :>> baseType = causes meta SysML::Usage;
    }
}
