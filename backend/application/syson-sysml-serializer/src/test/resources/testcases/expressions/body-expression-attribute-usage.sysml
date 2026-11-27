// Covers a body expression that owns an attribute usage
// (found with the validation case checkExpressionResultBindingConnector-trigger-in-case-of-unrelated-binding)
package BodyExpressionAttributeUsage {
    attribute collected = (1, 2).{
        in operand : ScalarValues::Integer;
        attribute bias : ScalarValues::Integer;
        operand + bias
    };
}
