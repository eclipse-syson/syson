action def A1 {
    part def P1 {
        isValid : ScalarValues::Boolean;
    }

    action def A2 {
        out prA2 : P1;
    }

    action a2 : A2 {
        out pra2;
    }
    if a2.pra2.isValid then a3;
    action a3 {}
}
