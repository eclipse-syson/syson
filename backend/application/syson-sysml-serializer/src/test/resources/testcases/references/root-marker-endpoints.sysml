// Covers the $ root marker of qualified endpoints and typings, kept unquoted, next to names spelled '$'
// (SysMLNameFormatter#quoteQualifiedNameSegments)
package P {
    part def A;
    port def Pt {
        out attribute x;
    }
    part a {
        port p : Pt;
    }
    part b {
        port p : ~Pt;
    }
    part '$';
}
package '$' {
    part c;
}
part def Q {
    part c : $::P::A;
    port d : ~$::P::Pt;
    connect $::P::a to $::P::b;
    interface $::P::a.p to $::P::b.p;
    flow from $::P::a.p.x to $::P::b.p.x;
    bind $::P::a = $::P::b;
    first $::P::a then $::P::b;
    allocate $::P::a to $::P::b;
    connect P::'$' to '$'::c;
    connect $::'$'::c to $::P::'$';
}
