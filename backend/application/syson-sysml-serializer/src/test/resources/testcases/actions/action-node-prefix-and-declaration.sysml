// Covers occurrence usage prefixes and usage declarations of control nodes and their body parameters (ControlActionRenderer)
package ActionNodePrefixAndDeclaration {
    action def A;
    metadata def K;
    action def Run {
        attribute x[*];
        action q;
        individual action b : A if true {
        }
        abstract action <w1> w : A[1] while true {
        }
        ref action f :> q for i in x {
        }
        snapshot action l loop action body : A {
        }
        until false;
        #K if false {
        } else action e : A {
        }
        derived terminate;
        in action t terminate;
    }
}
