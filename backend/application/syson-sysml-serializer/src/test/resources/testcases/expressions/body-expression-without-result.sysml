// Covers a body expression that declares a parameter and no result expression
// (found with the validation case checkExpressionResultBindingConnector-conforming)
package BodyExpressionWithoutResult {
    attribute collected = (1, 2).{
        in operand : ScalarValues::Integer; // final parameter
    };
}
