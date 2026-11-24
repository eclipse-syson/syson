package AssertReferencedFeatureChain {
    part enclosing {
        assert constraints.cost;
        assert not constraints.cost {
            redefines limit = source.limit.value;
        }
        assert constraints.limit :>> bound;
        assert limit :>> bound [1];
    }
}
