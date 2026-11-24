// NameRendererTest names the definitions T: their name is already exposed by the short name of A, or by an alias, so
// they must be qualified by their short name S (NameRenderer#referenceName)
package P {
    part def <T> A;
    part def <S> T;
    part p : S;
}
package P2 {
    part def X;
    alias T for X;
    part def <S> T;
    part q : S;
}
