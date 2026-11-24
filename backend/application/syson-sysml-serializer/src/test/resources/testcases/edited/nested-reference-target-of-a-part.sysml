// NestedTargetSearchTest links the reference subsetting of p to q, then searches P for a nested reference usage target:
// p is a part usage, not a reference usage, so none must be found (NestedTargetSearch#nestedReferenceTarget)
part q;
part def P {
    part p ::> q;
}
