// Covers a conditional used as the condition of a conditional (OperatorExpressionRenderer#conditionalText)
package ConditionalConditionConditional {
    attribute nested = if (if a ? b else c) ? d else e;
}
