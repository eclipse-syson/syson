// Covers imports and members in message bodies (FlowRenderer)
package MessageBodyMembers {
    package Q {
        part def X;
    }
    part a;
    part b;
    message m1 from a to b {
        private import Q::*;
    }
    message m2 of Q::X from a to b {
        attribute x;
        // trailing note in a message body
    }
}
