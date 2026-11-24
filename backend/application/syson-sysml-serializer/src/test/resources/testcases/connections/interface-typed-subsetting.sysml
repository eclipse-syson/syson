// Covers interface specializations kept alongside typing in the connect form (InterfaceRenderer)
package InterfaceTypedSubsetting {
    interface def I;
    part a;
    part b;
    interface j connect a to b;
    interface i1 : I :> j connect a to b;
    interface i2 : I :>> j connect a to b;
    interface i3 : I :> j, j connect a to b;
    interface <s> i4 : I :> j [1] connect a to b;
    interface i5 :> j connect a to b;
    interface i6 [1] : I :> j connect a to b;
    interface i7 : I [1] connect a to b;
    interface [2] connect a to b;
    interface : I = j connect a to b;
}
