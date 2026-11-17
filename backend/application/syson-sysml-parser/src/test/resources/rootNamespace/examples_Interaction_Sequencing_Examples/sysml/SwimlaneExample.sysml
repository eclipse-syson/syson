package SwimlaneExample {
action def A {
    action a1;
    then action a2;
    then action a3;
    then action a4;
}

part def P {
    perform action a : A;
    part p1 {
        perform a.a1;
        perform a.a4;
    }
    part p2 {
        perform a.a2;
        perform a.a3;
    }
}

part def System;
part def Person;

use case def UC {
    subject system : System;
    actor person : Person;
    
    first start;
    then action step1;
    then action step2;
    then action step3;
    then action step4;
    then done;    
}

part system_person_collaboration {
    // "include" is the equivalent of "perform" for a use case
    include use case uc : UC {
        subject :>> system = s;
        actor :>> person = p;
    }
    part s : System {
        perform uc.step3;
        perform uc.step4;
    }
    part p : Person {
        perform uc.step1;
        perform uc.step2;
    }
}
}