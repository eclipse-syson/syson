// SysMLHtmlModelTraceabilityTest links the typing of t to the conjugated definition that Pt owns: in the HTML output,
// the qualifier P must link to its package, like the qualifier of any other typing (ExpressionReferenceCollector)
package P {
    port def Pt;
}
part def Q {
    port t : ~P::Pt;
}
