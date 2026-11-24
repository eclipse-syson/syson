// Extracted from ImportSysMLModelTest#checkBasicMetadaUsage
package p1 {
    metadata def MD1 {
        attribute x : ScalarValues::String;
    }
    metadata def MD2 {
        attribute y : ScalarValues::String;
        :> annotatedElement : SysML::PartUsage;
    }

    #MD1 part p1;
    part p2 {
        @MD1 {
            x = "1";
    }
    }
    part p3;
    metadata MD1 about p3;
    part p4;
    metadata m1 : MD1 about p4;

    #MD2 part p5;
}
