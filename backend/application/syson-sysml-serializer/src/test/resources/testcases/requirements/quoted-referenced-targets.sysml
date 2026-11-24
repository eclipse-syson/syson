// Covers referenced targets whose source quoted the last name segment, with and without qualification
// (ReferenceResolver#directReferencedElementTarget)
package QuotedReferencedTargets {
    package P {
        concern c;
        concern 'd e';
        occurrence o;
        part a {
            occurrence e;
            concern k;
        }
        rendering rr;
        requirement r;
    }
    viewpoint def V {
        frame P::'c';
        frame P::'d e';
        frame P::c;
        frame P::a.'k';
    }
    occurrence def O {
        event P::'o';
        event P::a.'e';
    }
    view def VD {
        render P::'rr';
    }
    requirement def R {
        require P::'r';
        frame P::a.'k';
    }
}
