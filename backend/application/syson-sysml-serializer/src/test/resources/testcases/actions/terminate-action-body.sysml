// Covers terminate action body and explicit action keyword (ActionRenderer)
package TerminateActionBody {
    action def Run {
        action p;
        action t terminate p {
            doc /* d */
        }
        action terminate;
    }
}
