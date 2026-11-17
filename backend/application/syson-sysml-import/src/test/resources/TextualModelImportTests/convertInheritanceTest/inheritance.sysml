package InheritanceTest {
    part def Def {
        part a;
    }
    part p : Def {
        b :>> a;
    }
}