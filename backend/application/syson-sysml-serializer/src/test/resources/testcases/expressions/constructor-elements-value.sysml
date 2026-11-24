package ConstructorElements {
    attribute def Point;
    attribute points : Collections::List {
        attribute :>> elements : Point = (new Point());
    }
}
