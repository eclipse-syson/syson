// NameRendererTest marks the membership of helper implied and moves it before the typing of p, then serializes with a
// contextual name resolver: the alias of the omitted helper is not the reference site of the typing (NameRenderer)
package P {
    part def T;
    part p : T {
        part helper {
            alias LocalT for T;
        }
    }
}
