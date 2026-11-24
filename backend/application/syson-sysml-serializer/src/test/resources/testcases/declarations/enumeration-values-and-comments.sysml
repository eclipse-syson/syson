package EnumerationValuesAndComments {
    enum def Status {
        doc /* Status values are intentionally mixed. */
        enum ready;
        running;
        failed; // Keep the final value comment.
    }

    enum current : Status = Status::ready;
}
