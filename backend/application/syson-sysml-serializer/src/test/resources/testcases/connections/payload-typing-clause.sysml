package PayloadTypingClause {
    flow clause of : Data from source to destination;
    flow bare of Data from source to destination;
    flow multiplicityThenClause of [1] : Data from source to destination;
    flow multiplicityThenBare of [1] Data from source to destination;
    flow keywordClause of defined by Data from source to destination;
    message notified of : Data from source to destination;
    action receive {
        accept : Data;
        accept Data;
        accept [1] : Data via inlet;
    }
}
