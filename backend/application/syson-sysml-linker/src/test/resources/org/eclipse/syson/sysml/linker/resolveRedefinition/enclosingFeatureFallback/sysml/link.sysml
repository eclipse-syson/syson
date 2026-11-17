// With no inherited target, the reference implementation resolves P::target.
package Bases {
    part def Base;
}
package P {
    part target;
    part def Derived :> Bases::Base {
        part replacement :>> target;
    }
}
