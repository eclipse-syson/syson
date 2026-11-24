package FramedConcernBodyFeatureChains {
    requirement enclosingRequirement {
        frame concerns.cost {
            redefines firstPayload = firstSource.firstPayload.value;
            redefines secondPayload = secondSource.secondPayload.value;
        }
    }
}
