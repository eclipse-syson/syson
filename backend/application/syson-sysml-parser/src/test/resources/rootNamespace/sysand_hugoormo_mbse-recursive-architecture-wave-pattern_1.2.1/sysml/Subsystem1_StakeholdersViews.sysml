package Subsystem1_StakeholdersAndConcerns {
        part Subsystem1Designer {
            doc
            /* The Subsystem1Designer is a stakeholder with subsystem level concerns */
        }
        concern subsystemOptimalCost {
            doc
            /* The cost of the subsystem1 shall be designed optimally knowing that the
             * cost per quality gain of the prebuilder is twice as high as the cost per
             * quality gain of the finisher.
             * In this reference pattern, component envelopes are modeled as exogenous
             * technology limits to avoid circular allocation derivation.
             */
            subject Subsystem subsets Subsystem1_Configuration::payloadBuilder_gb;
            stakeholder user subsets Subsystem1Designer;

            attribute payloadPrebuilderTechnologyEnvelopeMax defined by ScalarValues::Real [1] = 4.0;
            attribute payloadFinisherTechnologyEnvelopeMin defined by ScalarValues::Real [1] = 10.0;
            attribute payloadFinisherToPrebuilderMinRatio defined by ScalarValues::Real [1] = 2.0;

            item prebuiltPayload subsets Subsystem1_Configuration::subsystem1Payloads; 
            item finishedPayload subsets Subsystem1_Configuration::subsystem1Payloads; 

            require constraint {
                finishedPayload.quality >= prebuiltPayload.quality * payloadFinisherToPrebuilderMinRatio
            }

            require constraint {
                prebuiltPayload.quality <= payloadPrebuilderTechnologyEnvelopeMax
            }

            require constraint {
                finishedPayload.quality >= payloadFinisherTechnologyEnvelopeMin
            }
        }
    }

package Subsystem1_ViewpointsAndViews {
    doc
    /* Viewpoints and Views for Subsystem1 (Payload Builder) at L2.
     * These provide stakeholder-centric perspectives on the subsystem decomposition.
     */

    viewpoint costAllocationStrategy {
        doc
        /* The cost allocation viewpoint focuses on how subsystem1 (Payload Builder)
         * decomposes its quality contribution across the prebuilder and finisher components.
         * This addresses the concern of optimal cost per quality gain.
         */
        frame Subsystem1_StakeholdersAndConcerns::subsystemOptimalCost;
    }

    view costAllocationL2 {
        doc
        /* The cost allocation view at L2 exposes how subsystem1 (Payload Builder)
         * distributes quality allocations to its components.
         * The prebuilder receives 4.0 units (28.6%), finisher receives 10.0 units (71.4%).
         */
        viewpoint costAllocationStrategy;
        expose Subsystem1_Configuration::payloadBuilder_gb;
    }
}