// NameRendererTest replaces the types of x, y and z by unresolved proxies, as if their library were not loaded, whose
// URI fragments are the names part, array-membership and P::part: each must be named by a valid reference
// (NameRenderer#referenceName)
part def 'part';
part def 'array-membership';
package P {
    part def 'part';
}
part x : 'part';
part y : 'array-membership';
part z : P::'part';
