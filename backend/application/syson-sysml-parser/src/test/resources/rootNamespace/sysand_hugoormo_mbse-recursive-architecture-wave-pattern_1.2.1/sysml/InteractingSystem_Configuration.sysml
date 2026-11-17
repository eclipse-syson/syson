package InteractingSystem_Configuration {
    doc
    /* The interacting system at level 1 is described in this package.
     * The blackbox interacting system represents an external operational participant.
     * It is terminal in this reference pattern and is not recursively decomposed.
     */

    item myInteractingSystemPayloads subsets InteractingSystem_Taxonomy::myInteractingSystemExchangeItems subsets Operational_Configuration::operationalPayloads;
    item myInteractingSystemFeedbacks subsets InteractingSystem_Taxonomy::myInteractingSystemExchangeItems subsets Operational_Configuration::operationalFeedbacks;

    part interactingSystem_bb subsets MyStandard::myStandardComplyingPart subsets InteractingSystem_Taxonomy::myInteractingSystems [1] {
        doc
        /* The blackbox interacting system represents the interacting system as a blackbox,
         * by way of example compliant with the quality gain defined by the MyStandard.
         * The requirements satisfaction against the MyStandard is inherited from the 
         * operationalGainForMyStandard.
         */

        comment interactingSystemBbModelerComment about interactingSystem_bb locale "en_US"
        /* External-system capability follows the L0 allocation budget;
         * the L0 requirement is the source of truth.
         */
        attribute qualityCapabilitySetting
            default Operational_Requirements::payloadQualityGainOperationalDerived.operationalPayloadThroughInteractingSystemGain;
        attribute realizedQualityContribution = consumePayload_bb.consumedPayload.quality - consumePayload_bb.producedPayload.quality;
        attribute redefines qualityContribution = realizedQualityContribution;

        perform action consumePayload_bb references InteractingSystem_Behavior::consumePayload_bb{
            in qualityCapabilitySetting = interactingSystem_bb.qualityCapabilitySetting;
        }
    }
}
