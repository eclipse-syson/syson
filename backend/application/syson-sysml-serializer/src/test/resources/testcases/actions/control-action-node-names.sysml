// Covers named if and for action nodes (ControlActionRenderer)
package ControlActionNodeNames {
    action def Run {
        attribute x[*];
        action c if true {
        }
        action d for i in x {
        }
        action if false {
        }
    }
}
