package ReferenceUsageReferencedFeature {
    part enclosing {
        ref ::> plain;
        ref ::> things.thing;
        ref ::> thing [2];
        in ref ::> thing;
        ref ::> thing {
            attribute delay;
        }
        ref ::> things.thing {
            redefines delay = source.delay.value;
        }
    }
}
