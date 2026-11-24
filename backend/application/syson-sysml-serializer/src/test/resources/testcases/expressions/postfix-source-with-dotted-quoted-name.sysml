// Covers a chain source spelled with a quoted name that contains dots (ExpressionPathSupport)
package PostfixSourceWithDottedQuotedName {
    package Q {
        part 'a.b';
    }
    attribute resolvedSource = Q::'a.b'.c;
    attribute unresolvedSource = R::'a.b'.c;
}
