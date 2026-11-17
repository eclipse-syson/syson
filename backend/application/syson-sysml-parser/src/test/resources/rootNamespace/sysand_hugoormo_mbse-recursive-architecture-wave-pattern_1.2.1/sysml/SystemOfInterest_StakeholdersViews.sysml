package SystemOfInterest_StakeholdersAndConcerns {
    part SystemOfInterestUser {
        doc
        /* The SystemOfInterestUser is a stakeholder with system level concerns */
    }
    concern systemOptimalQualityGainEffort {
        doc
        /* The quality gain effort throught the system of interest shall be optimal 
         * knowing that the quality effort of the payloadVerifier is twice as 
         * high as the quality gain of the payloadBuilder.
         * In this reference pattern, the verifier cap and ratio are modeled as
         * exogenous technology-envelope assumptions for non-circular allocation.
         */
        subject SystemOfInterest;
        stakeholder user subsets SystemOfInterestUser;

        attribute payloadVerifierTechnologyEnvelopeMax defined by ScalarValues::Real [1] = 7.0;
        attribute payloadBuilderToVerifierMinRatio defined by ScalarValues::Real [1] = 2.0;

        item builtPayload subsets SystemOfInterest_Configuration::systemPayloads; 
        item verifiedPayload subsets SystemOfInterest_Configuration::systemPayloads; 

        require constraint {
            builtPayload.quality >= verifiedPayload.quality * payloadBuilderToVerifierMinRatio
        }

        require constraint {
            verifiedPayload.quality <= payloadVerifierTechnologyEnvelopeMax
        }
    }
}

package SystemOfInterest_ViewpointsAndViews {
    doc
    /* Viewpoints and Views for the System of Interest (L1) level.
     * These provide stakeholder-centric perspectives on the system decomposition.
     */

    viewpoint qualityAllocationStrategy {
        doc
        /* The quality allocation viewpoint focuses on how the system of interest
         * decomposes its quality contribution across the builder and verifier subsystems.
         * This addresses the concern of optimal quality gain effort.
         */
        frame SystemOfInterest_StakeholdersAndConcerns::systemOptimalQualityGainEffort;
    }

    view qualityAllocationL1 {
        doc
        /* The quality allocation view at L1 exposes how the system of interest
         * distributes quality allocations to its subsystems.
         * The builder receives 14.0 units (70%), verifier receives 7.0 units (30%).
         */
        viewpoint qualityAllocationStrategy;
        expose SystemOfInterest_Configuration::systemOfInterest_gb;
    }
}

