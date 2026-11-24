// Covers imports, aliases and definitions in succession bodies (SuccessionRenderer)
package SuccessionBodyMembers {
    package Q {
        part def X;
    }
    part a;
    part b;
    succession s1 first a then b {
        private import Q::*;
    }
    succession s2 first a then b {
        alias A for a;
    }
    first a then b {
        part def Y;
    }
    action def D {
        action c;
        action d;
        first c then d {
            private import Q::X;
        }
    }
}
