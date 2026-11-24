action def P1 {

    action def A2 {
        out pr2 : ScalarValues::Boolean;
    }

    action a2 : A2 {
         out pr2;
    }
    if a2.pr2 then a3;
    action a3 {}
}
