package Component1_2_StakeholdersAndConcerns {
    doc
    /* Component1_2 (Payload Finisher) is a leaf design element.
     * This package formalizes the stakeholder concerns that define the design contract.
     * At this level, decomposition ends and detailed design begins.
     * The component designer receives these concerns as requirements to satisfy.
     */

    part Component1_2Designer {
        doc
        /* The Component1_2Designer is a stakeholder responsible for the detailed design
         * and implementation of the Payload Finisher component.
         * This designer receives the quality allocation as a design requirement.
         */
    }

    concern component1_2QualityAllocation {
        doc
        /* The Payload Finisher component shall contribute a minimum quality gain of 10.0 units
         * to enable optimal subsystem1 (Payload Builder) integration.
         * This is the design contract that defines the scope and success criteria for this component.
         */
        subject payloadFinisher subsets Component1_2_Configuration::payloadFinisher_bb;
        stakeholder designer subsets Component1_2Designer;

        require constraint {
            payloadFinisher.qualityContribution >= 10.0
        }
    }
}
