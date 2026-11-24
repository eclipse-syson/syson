// Covers while loop until clause equal to the while condition (ControlActionRenderer)
package WhileUntilEqualCondition {
    action def Run {
        action c while true {
            action x;
        }
        until true;
    }
}
