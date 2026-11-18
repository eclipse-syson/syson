package P {
    state active {
        accept x then idle {
            action outer {
                action inner;
            }
        }
        part retained;
        accept y then idle
    }
    part keep;
}
package Q;
