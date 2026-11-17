// Resolve Base::target instead of the unrelated same-named feature enclosing Derived.
package Bases {
    part def Base {
        part target;
    }
}
package P {
    part target;
    part def Derived :> Bases::Base {
        part replacement :>> target;
    }
}
