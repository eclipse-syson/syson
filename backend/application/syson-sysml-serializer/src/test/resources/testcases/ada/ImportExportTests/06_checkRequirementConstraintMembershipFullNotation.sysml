private import SI::kilogram;
private import ScalarValues::*;
part def P1 {
    attribute x : Real;
}
requirement r1 {
    subject sub : P1;
    attribute actualMass :> ISQBase::mass;
    attribute maxMass :> ISQBase::mass;
    assume constraint NamedRequirementConstraint {
        actualMass <= maxMass
    }
    assume constraint {
        sub.x <= 500
    }
    require constraint {
        actualMass >= 500 [kg]
    }
}
constraint def C;
constraint c : C;
requirement def R1 {
    require constraint c1 :>> c;
}
