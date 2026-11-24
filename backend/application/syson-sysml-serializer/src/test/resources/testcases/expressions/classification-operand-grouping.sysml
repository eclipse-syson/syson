// Covers a classification used as the operand of a tighter operator (OperatorExpressionRenderer#classificationOperatorText)
package ClassificationOperandGrouping {
    attribute def T;
    attribute castSum = (x as T) + 1;
    attribute testSum = (x istype T) + 1;
    attribute castProduct = (x as T) * 2;
}
