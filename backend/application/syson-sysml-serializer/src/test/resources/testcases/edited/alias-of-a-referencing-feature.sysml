// SysMLHtmlModelTraceabilityTest links q and p and writes HTML: the alias target p references q, but the link of the
// alias must lead to p, the feature it names (ExpressionAndReferenceWriter#writeReferencePath)
part q;
part p ::> q;
alias a for p;
