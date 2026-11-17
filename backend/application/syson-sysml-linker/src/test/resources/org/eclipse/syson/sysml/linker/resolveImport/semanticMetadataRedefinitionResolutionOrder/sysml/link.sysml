// Reproduces a redefinition resolved through MetadataItem before the explicit SemanticMetadata hierarchy.
package Metaobjects {
    abstract metadata def Metaobject {
        ref annotatedElement;
    }

    abstract metadata def SemanticMetadata :> Metaobject {
        ref :>> annotatedElement;
    }
}

package Metadata {
    abstract metadata def MetadataItem :> Metaobjects::Metaobject;
}

package Test {
    private import Metaobjects::SemanticMetadata;

    metadata def TestMetadata :> SemanticMetadata {
        // Fails: expected SemanticMetadata::annotatedElement, but resolves to Metaobject::annotatedElement.
        ref :>> annotatedElement;
    }

    part def Annotated {
        @TestMetadata;
    }
}
