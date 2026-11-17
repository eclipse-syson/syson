package Component1_1_Configuration {
    doc
    /* Components won't be decomposed any further.
     * Components are therefore modeled solely as Blackboxes.
     */

    item component1_1Payloads subsets Component1_1_Taxonomy::myComponent1_1ExchangeItems subsets Subsystem1_Configuration::subsystem1Payloads;
    item component1_1Feedbacks subsets Component1_1_Taxonomy::myComponent1_1ExchangeItems subsets Subsystem1_Configuration::subsystem1Feedbacks;

    part payloadPrebuilder_bb subsets Component1_1_Taxonomy::myComponents1_1 {
        comment payloadPrebuilderBbModelerComment about payloadPrebuilder_bb locale "en_US"
        /* Leaf requirement: realized contribution comes from behavioral output,
         * while capability is provided as an independent seeded input.
         */
        attribute qualityCapabilitySetting defined by ScalarValues::Real [1]
            default Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughPrebuilderGain;
        attribute realizedQualityContribution = prebuildPayload_bb.prebuiltPayload.quality;
        attribute redefines qualityContribution = realizedQualityContribution;

        perform action prebuildPayload_bb references Component1_1_Behavior::prebuildPayload_bb {
            in qualityCapabilitySetting = payloadPrebuilder_bb.qualityCapabilitySetting;
        }

        satisfy Component1_1_Requirements::payloadQualityGainComponent1_1;
    }
}