// Covers metadata body field operators, typing and nested bodies (MetadataRenderer#serializeMetadataField)
package MetadataBodyFieldParts {
    attribute def T;
    metadata def M {
        attribute a;
        attribute b;
        attribute c;
        attribute d;
    }
    part p {
        metadata M {
            a := 1;
            b default = 2;
            c : T = 3;
            d = 4 { y = 5; }
        }
    }
}
