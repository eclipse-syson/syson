package Component1_2_Configuration {
    doc
    /* Components won't be decomposed any further.
     * Components are therefore modeled solely as Blackboxes.
     */

    item component1_2Payloads subsets Component1_2_Taxonomy::myComponent1_2ExchangeItems subsets Subsystem1_Configuration::subsystem1Payloads;
    item component1_2Feedbacks subsets Component1_2_Taxonomy::myComponent1_2ExchangeItems subsets Subsystem1_Configuration::subsystem1Feedbacks;

    part payloadFinisher_bb subsets Component1_2_Taxonomy::myComponents1_2 {
        comment payloadFinisherBbModelerComment about payloadFinisher_bb locale "en_US"
        /* Leaf requirement: realized contribution comes from behavioral output,
         * while capability is provided as an independent seeded input.
         */
        attribute qualityCapabilitySetting defined by ScalarValues::Real [1]
            default Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughFinisherGain;
        attribute realizedQualityContribution = finishPayload_bb.finishedPayload.quality - finishPayload_bb.prebuiltPayload.quality;
        attribute redefines qualityContribution = realizedQualityContribution;
        perform action finishPayload_bb references Component1_2_Behavior::finishPayload_bb{
            in qualityCapabilitySetting = payloadFinisher_bb.qualityCapabilitySetting;
        }

        satisfy Component1_2_Requirements::payloadQualityGainComponent1_2; 
    }
}