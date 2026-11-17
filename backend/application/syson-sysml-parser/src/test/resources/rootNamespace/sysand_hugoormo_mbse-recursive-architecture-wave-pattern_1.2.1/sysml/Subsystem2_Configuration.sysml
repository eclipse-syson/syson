package Subsystem2_Configuration {
    doc
    /* By way of example Subsystem2 represents a COTS and therefore won't be decomposed any further.
     * Subsystem2 is therefore modeled solely as a Blackbox.
     */

    item subsystem2Payloads subsets Subsystem2_Taxonomy::mySubsystem2ExchangeItems subsets SystemOfInterest_Configuration::systemPayloads;
    item subsystem2Feedbacks subsets Subsystem2_Taxonomy::mySubsystem2ExchangeItems subsets SystemOfInterest_Configuration::systemFeedbacks;

    part payloadVerifier_bb
        subsets Subsystem2_Taxonomy::mySubsystems2
        subsets MyCOTSLibrary::PayloadVerifierCOTSProductPackage::payloadVerifierCOTSProduct {
        doc
        /* This terminal payload verifier is modeled as the project usage of a
         * selected COTS library product. Its behavior remains represented for
         * analysis and verification of the reference pattern, but the interface
         * boundary is supplier-owned and constrained by MyCOTSLibrary.
         */

        comment payloadVerifierBbModelerComment about payloadVerifier_bb locale "en_US"
        /* Terminal requirement: realized contribution comes from behavioral output,
         * while capability is provided as an independent seeded input.
         */
        attribute qualityCapabilitySetting defined by ScalarValues::Real [1]
            default SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain;
        attribute realizedQualityContribution = verifyPayload_bb.verifiedPayload.quality - verifyPayload_bb.builtPayload.quality;
        attribute redefines qualityContribution = realizedQualityContribution;
        perform action verifyPayload_bb references Subsystem2_Behavior::verifyPayload_bb {
            in qualityCapabilitySetting = payloadVerifier_bb.qualityCapabilitySetting;
        }

        satisfy Subsystem2_Requirements::payloadQualityGainPayloadVerifier;
    }
}
