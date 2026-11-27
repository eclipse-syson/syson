// Covers a note ending the line that opens a body expression
// (found with the validation case checkExpressionResultBindingConnector-trigger-in-case-of-collect-body-without-binding)
package BodyExpressionNote {
    attribute collected = (1, 2).{ // note after the opening brace of a body expression
        in operand : ScalarValues::Integer;
        operand + operand
    };
}
