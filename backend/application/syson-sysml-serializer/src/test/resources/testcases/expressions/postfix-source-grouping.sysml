// Covers the grouping of postfix sources: #(), .{}, .?{}, feature chains and -> (OperatorPrecedence#mustParenthesizePostfixSource)
package PostfixSourceGrouping {
    attribute def T;
    attribute indexedSum = (a + b)#(1);
    attribute indexedCast = (x as T)#(1);
    attribute indexedTest = (x istype T)#(1);
    attribute selectedSum = (a + b).?{in v; true};
    attribute collectedSum = (a + b).{in v; v};
    attribute metaChain = (x meta T).y;
    attribute metadataTestChain = (x @ T).y;
    attribute extentChain = (all T).x;
    attribute extentNavigation = (all T)->size();
    attribute conditionalChain = (if c ? a else b).x;
}
