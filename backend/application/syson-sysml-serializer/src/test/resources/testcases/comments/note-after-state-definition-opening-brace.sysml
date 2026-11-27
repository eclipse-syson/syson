// Covers a note ending the line that opens the body of a state definition
// (found with the validation case validateStateDefinitionStateSubactionKind-trigger-in-case-of-two-do-actions)
package NoteAfterStateDefinitionOpeningBrace {
    state def Mode { // note after the opening brace of a state definition
        entry action prepare;
    }
}
