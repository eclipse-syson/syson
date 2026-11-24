// Covers trailing notes and empty blocks of interface and flow bodies (InterfaceRenderer, FlowRenderer)
package ConnectorBodyTrailingNotes {
    part a;
    part b;
    interface i connect a to b {
        // trailing note in an interface body
    }
    flow f from a to b {
        // trailing note in a flow body
    }
    flow of Integer from a to b {
        attribute x;
        // trailing note after a flow member
    }
    message m from a to b {
        // trailing note in a message body
    }
    interface j connect a to b {}
    flow g from a to b {}
}
