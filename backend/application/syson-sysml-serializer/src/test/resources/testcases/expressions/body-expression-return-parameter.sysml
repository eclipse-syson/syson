// Covers a body expression that declares a return parameter
// (found with the validation case validateExpressionResultParameterMembership-trigger-in-case-of-two-returns-in-collect-body)
package BodyExpressionReturnParameter {
    attribute collected = (1, 2).{
        in operand : ScalarValues::Integer;
        return primary : ScalarValues::Integer;
        operand
    };
}
