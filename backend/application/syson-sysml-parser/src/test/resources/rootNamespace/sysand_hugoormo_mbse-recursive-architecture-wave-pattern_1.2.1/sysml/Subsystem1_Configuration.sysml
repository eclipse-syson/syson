package Subsystem1_Configuration {

    item subsystem1Payloads subsets Subsystem1_Taxonomy::mySubsystem1ExchangeItems subsets SystemOfInterest_Configuration::systemPayloads;
    item subsystem1Feedbacks subsets Subsystem1_Taxonomy::mySubsystem1ExchangeItems subsets SystemOfInterest_Configuration::systemFeedbacks;

    part payloadBuilder_bb subsets Subsystem1_Taxonomy::mySubsystems1 {
        comment payloadBuilderBbModelerComment about payloadBuilder_bb locale "en_US"
        /* Original requirement: realized contribution comes from behavioral output,
         * while capability is provided as an independent seeded input.
         */
        attribute qualityCapabilitySetting defined by ScalarValues::Real [1]
            default
                Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughPrebuilderGain
                + Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived.subsystemPayloadThroughFinisherGain;
        attribute realizedQualityContribution = buildPayload_bb.builtPayload.quality;
        attribute redefines qualityContribution = realizedQualityContribution;
        attribute maintenanceStrain = 0.0;
        attribute strainPenaltyFactor = 0.1;
        attribute qualityContributionAdjusted = qualityContribution - (strainPenaltyFactor * maintenanceStrain);
        perform action buildPayload_bb references Subsystem1_Behavior::buildPayload_bb {
            in qualityCapabilitySetting = payloadBuilder_bb.qualityCapabilitySetting;
        }

        satisfy Subsystem1_Requirements::payloadQualityGainPayloadBuilder;
    }

    part payloadBuilder_gb subsets payloadBuilder_bb {
        comment payloadBuilderGbModelerComment about payloadBuilder_gb locale "en_US"
        /* Derived requirement: qualityContribution is implicitly sum of constituents;
         * validated by requirement constraint. Once we have a graybox, the
         * qualityGain will be defined by its constituents.
         */

        part payloadPrebuilder subsets Component1_1_Configuration::payloadPrebuilder_bb;
        part payloadFinisher subsets Component1_2_Configuration::payloadFinisher_bb;

        perform action buildPayload_gb
            redefines buildPayload_bb references Subsystem1_Behavior::buildPayload_gb;

        connection system1Connections_gb [1] subsets Subsystem1_Taxonomy::mySubsystem1Connections {
            end connectingPart [1] redefines myConnectionSource references payloadPrebuilder;
            end connectedPart [1] redefines myConnectionTarget references payloadFinisher; 
            doc
            /* This connection connects the action parameters of the interacting parties
             * without creating ports and therefore without using an interface.
             */
            flow prebuiltPayload subsets mySystemFlow [1]
                from connectingPart.prebuildPayload_bb.prebuiltPayload
                to connectedPart.finishPayload_bb.finishedPayloadFeedback;
            flow finishedFeedback subsets mySystemFlow [1]
                from connectedPart.finishPayload_bb.finishedPayload
                to connectingPart.prebuildPayload_bb.prebuiltPayloadFeedback;
        }

        satisfy Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived;
    }

    comment subsystem1ConfigurationModelerComment about Subsystem1_Configuration locale "en_US"
    /* Components are not decomposed any further than blackboxes.
     * Therefore Subsystem1 is only declared as a graybox and not as a whitebox.
     */
}
