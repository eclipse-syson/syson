// SysMLHtmlModelTraceabilityTest links the typings and writes HTML: the qualifier P, repeated by each typing of a list,
// must be linked at each place, in both typing clause forms (ExpressionAndReferenceWriter#writeTypingReferences)
package P {
    part def A;
    part def B;
}
part p : P::A, P::B;
part q defined by P::A, P::B;
