// Covers imports and aliases, which own no element, in action and connector bodies (ElementBodyRenderer)
package BodyImportsWithoutOwnedElements {
    package Q {
        part def X;
    }
    part a;
    part b;
    action def D {
        action t;
        perform action {
            private import Q::*;
            alias T for t;
        }
        action u terminate {
            private import Q::X;
        }
    }
    connection c connect a to b {
        private import Q::*;
    }
    allocation l allocate a to b {
        private import Q::X;
    }
    binding k bind a = b {
        alias A for a;
    }
}
