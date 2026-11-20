package Redefinition {
    part def Vehicle {
        part wheel {
            attribute diameter;
        }
    }
    part vehicle : Vehicle;
    view view1 : StandardViewDefinitions::GeneralView {
        expose vehicle;
    }
}
