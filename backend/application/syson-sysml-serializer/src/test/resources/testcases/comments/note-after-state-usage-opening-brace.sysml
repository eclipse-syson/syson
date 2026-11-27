// Covers a note ending the line that opens the body of a state usage
// (found with the validation case validateStateUsageStateSubactionKind-trigger-in-case-of-two-do-actions)
package NoteAfterStateUsageOpeningBrace {
    state def SystemMode {
        state mode { // note after the opening brace of a state usage
            entry action prepare;
        }
    }
}
