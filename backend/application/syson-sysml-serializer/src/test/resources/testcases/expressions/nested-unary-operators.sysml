// Covers unary operators applied to unary operands (OperatorExpressionRenderer#notText, #prefixUnaryText)
package NestedUnaryOperators {
    attribute doubleNot = not (not a);
    attribute notNegation = not (-a);
    attribute negatedNot = -(not a);
    attribute conjugatedNot = ~(not a);
}
