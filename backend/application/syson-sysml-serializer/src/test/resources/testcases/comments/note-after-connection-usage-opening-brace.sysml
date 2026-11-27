// Covers a note ending the line that opens the body of a connection usage
// (found with the validation case validateConnectorRelatedFeatures-trigger-in-case-of-one-end)
package NoteAfterConnectionUsageOpeningBrace {
    part def Holder {
        part origin;
        connection link { // note after the opening brace of a connection usage
            end ::> origin;
        }
    }
}
