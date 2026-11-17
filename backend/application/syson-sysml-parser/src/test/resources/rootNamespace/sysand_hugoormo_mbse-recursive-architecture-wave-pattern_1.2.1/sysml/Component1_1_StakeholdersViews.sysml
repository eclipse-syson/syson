package Component1_1_StakeholdersAndConcerns {
    doc
    /* Component1_1 (Payload Prebuilder) is a leaf design element.
     * This package formalizes the stakeholder concerns that define the design contract.
     * At this level, decomposition ends and detailed design begins.
     * The component designer receives these concerns as requirements to satisfy.
     */

    part Component1_1Designer {
        doc
        /* The Component1_1Designer is a stakeholder responsible for the detailed design
         * and implementation of the Payload Prebuilder component.
         * This designer receives the quality allocation as a design requirement.
         */
    }

    concern component1_1QualityAllocation {
        doc
        /* The Payload Prebuilder component shall contribute a minimum quality gain of 4.0 units
         * to enable optimal subsystem1 (Payload Builder) integration.
         * This is the design contract that defines the scope and success criteria for this component.
         */
        subject payloadPrebuilder subsets Component1_1_Configuration::payloadPrebuilder_bb;
        stakeholder designer subsets Component1_1Designer;

        require constraint {
            payloadPrebuilder.qualityContribution >= 4.0
        }
    }
}
