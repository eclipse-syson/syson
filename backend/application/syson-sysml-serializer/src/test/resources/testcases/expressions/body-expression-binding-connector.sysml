// Covers a body expression that owns a binding connector
// (found with the validation case checkExpressionResultBindingConnector-trigger-in-case-of-unrelated-binding)
package BodyExpressionBindingConnector {
    attribute collected = (1, 2).{
        in operand : ScalarValues::Integer;
        bind operand = operand;
        operand
    };
}
