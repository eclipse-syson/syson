// Covers the action body of an unnamed terminate without occurrence (ActionRenderer)
package AnonymousTerminateBody {
    action def Run {
        terminate {
            action b;
        }
    }
}
