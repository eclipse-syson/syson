package FirstFeatureChainingContext {
    part def Node;

    part root : Node {
        public part child : Node;
    }

    // The first segment context is determined as for the Subsetting
    // that owns the anonymous chained feature.
    part subsettingChain subsets root.child {
        // These local features must not shadow the package features while the
        // first chaining feature is resolved in the Subsetting context.
        private part root : Node {
            public part child : Node;
        }
    }
}
