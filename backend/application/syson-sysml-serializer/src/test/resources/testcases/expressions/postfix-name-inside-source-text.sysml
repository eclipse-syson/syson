// Covers unresolved chained names also spelled inside the source text (ExpressionPathSupport)
package PostfixNameInsideSourceText {
    attribute insideLongerName = (b + ab).a;
    attribute insideString = ("a" + 1).a;
    attribute repeatedName = (a + 1).a;
    attribute insideArgument = f(ab, "a").a;
}
