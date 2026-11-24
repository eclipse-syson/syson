package ArrayMultiplicity {
    private import Collections::*;
    attribute readings : Array [1] {
        attribute :>> dimensions = 2;
        attribute :>> elements : ScalarValues::Real [2] = (1.0, 2.0);
    }
}
