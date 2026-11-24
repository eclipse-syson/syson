// Covers the non-associative classification level (OperatorPrecedence#mustParenthesize)
package ClassificationNonAssociative {
    attribute def T;
    attribute def U;
    attribute doubleCast = (x as T) as U;
    attribute doubleTest = (x istype T) istype U;
}
