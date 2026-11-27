// Covers a note ending the line of a join node followed by a shorthand succession
// (found with the validation case validateJoinNodeOutgoingSuccessions-trigger-in-case-of-shorthand-succession)
package NoteAfterJoinNode {
    action def Route {
        join combine; // note after a join node
        then store;
        action store;
    }
}
