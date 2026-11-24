// Covers interface ends next to a body port (InterfaceRenderer)
package InterfaceEndsWithBodyPort {
    port def PD;
    interface def I {
        end port p1 : PD;
        end port p2 : PD;
    }
    part c {
        port a : PD;
        port b : PD;
        interface <s> i : I [1] connect a to b {
            port q;
        }
    }
}
