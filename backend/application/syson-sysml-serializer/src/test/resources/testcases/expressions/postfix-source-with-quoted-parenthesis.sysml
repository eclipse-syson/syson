// Covers a parenthesized postfix source whose string or name contains a parenthesis (ExpressionPathSupport)
package PostfixSourceWithQuotedParenthesis {
    attribute byName = ('x)y' + 1).c;
    attribute byString = (")" + 1).c;
    attribute byOpening = ("(" + 1).c;
}
