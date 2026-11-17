package Subsystem2_StakeholdersAndConcerns {
    doc
    /* Subsystem2 (Payload Verifier) is a terminal leaf subsystem at L2.
     * This package formalizes the stakeholder concerns that define the design contract.
     * Subsystem2 is not decomposed further; the verifier is a leaf component.
     */

    part Subsystem2Designer {
        doc
        /* The Subsystem2Designer is a stakeholder responsible for the design
         * and integration of the Payload Verifier subsystem.
         */
    }

    concern subsystem2QualityAllocation {
        doc
        /* Subsystem2 (Payload Verifier) shall contribute a minimum quality gain of 7.0 units
         * to enable optimal system of interest integration.
         * This mirrors the requirement subsetting from L1.
         */
        doc
        /* Subsystem2 (Payload Verifier) shall contribute a minimum quality gain of 7.0 units
         * to enable optimal system of interest integration.
         * This is the design contract that defines the scope and success criteria for the verifier.
         */
        subject payloadVerifier subsets Subsystem2_Configuration::payloadVerifier_bb;
        stakeholder designer subsets Subsystem2Designer;

        require constraint {
            payloadVerifier.qualityContribution >= 7.0
        }
    }
}

package Subsystem2_ViewpointsAndViews {
    doc
    /* Viewpoints and Views for Subsystem2 (Payload Verifier) at L2.
     * Since Subsystem2 is a terminal leaf, the viewpoint is simple: the quality allocation requirement.
     */

    viewpoint qualityAllocationLeaf {
        doc
        /* The quality allocation viewpoint for Subsystem2 (terminal leaf).
         * This addresses the concern that the verifier must meet its quality allocation target.
         */
        frame Subsystem2_StakeholdersAndConcerns::subsystem2QualityAllocation;
    }

    view qualityAllocationL2Subsystem2 {
        doc
        /* The quality allocation view at L2 for Subsystem2 exposes the verifier component
         * and its quality contribution requirement of 7.0 units.
         */
        viewpoint qualityAllocationLeaf;
        expose Subsystem2_Configuration::payloadVerifier_bb;
    }
}
