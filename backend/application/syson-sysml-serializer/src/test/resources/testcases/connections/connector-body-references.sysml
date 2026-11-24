// Covers reference declarations in the body of connector shorthands, which are not ends (ModelNavigation#referenceEnds)
package ConnectorBodyReferences {
    part def P {
        part a;
        part b;
        allocate a to b {
            ref extra1;
        }
        allocation al allocate (a, b) {
            ref extra2;
        }
        connect a to b {
            ref extra3;
        }
        bind a = b {
            ref extra4;
        }
        first a then b {
            ref extra5;
        }
        succession s first a then b {
            ref extra6;
        }
        connection c {
            end ref x1 ::> a;
            end ref x2 ::> b;
            ref extra7;
        }
    }
    action def A {
        action a1;
        action a2;
        succession s first a1 then a2 {
            ref extra8;
        }
    }
}
