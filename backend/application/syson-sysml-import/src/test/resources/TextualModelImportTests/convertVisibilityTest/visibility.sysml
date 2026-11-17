package VisibilityTest {
    item f : A;
    item def A {
        protected part c : C;
    }
    private part def C {
        private in y : A;
    }
}
