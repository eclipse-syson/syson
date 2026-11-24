package ReferencedFeatureBodyFeatureChains {
    requirement enclosingRequirement {
        require constraints.cost {
            redefines payload = source.payload.value;
        }
        assume cost {
            redefines payload = source.payload.value;
        }
    }
    verification enclosingVerification {
        objective {
            verify requirements.cost {
                redefines payload = source.payload.value;
            }
        }
    }
    use case enclosingUseCase {
        include useCases.nested {
            redefines payload = source.payload.value;
        }
    }
    part enclosingPart {
        perform run {
            redefines payload = source.payload.value;
        }
        exhibit on {
            redefines payload = source.payload.value;
        }
    }
    state enclosingState {
        do run {
            redefines payload = source.payload.value;
        }
    }
}
