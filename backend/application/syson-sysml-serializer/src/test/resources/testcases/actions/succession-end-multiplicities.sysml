// Covers the multiplicities of the source and target ends of a succession
// (found with the validation cases validateControlNodeIncomingSuccessions-conforming and
// validateControlNodeOutgoingSuccessions-conforming)
package SuccessionEndMultiplicities {
    action def Route {
        action prepare;
        fork dispatch;
        action archiveResult;
        succession first prepare then [1] dispatch;
        succession first [1] dispatch then archiveResult;
    }
}
