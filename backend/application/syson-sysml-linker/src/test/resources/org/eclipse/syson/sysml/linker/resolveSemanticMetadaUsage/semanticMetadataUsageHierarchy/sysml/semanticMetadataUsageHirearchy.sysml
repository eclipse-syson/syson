package pack1 {
    private import Metaobjects::SemanticMetadata;

    occurrence def Cause {
        attribute occurs;
    }

    abstract occurrence causes : Cause [*] nonunique;

    metadata def <cause> CauseMetadata :> SemanticMetadata {
        :>> baseType = causes meta SysML::Usage;
    }

    metadata def <cause2> CauseMetadata2 :> CauseMetadata {
    }

    #CauseMetadata2 occurrence 'battery depleted' {
        :>> occurs = 0.005;
    }
}
