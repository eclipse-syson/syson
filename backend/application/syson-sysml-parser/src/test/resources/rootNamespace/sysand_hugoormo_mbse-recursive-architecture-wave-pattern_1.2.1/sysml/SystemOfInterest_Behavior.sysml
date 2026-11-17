package SystemOfInterest_Behavior {
    doc /*
    * The action parameters of the subactions are bound to the action
    * parameters of the main action so that they can be passed between the
    * parts in the context of the upper level gray box; in this case the context_gb.
    */

    action producePayload_bb {
        in qualityCapabilitySetting defined by ScalarValues::Real;
        in item producedPayloadFeedback subsets SystemOfInterest_Configuration::systemFeedbacks;
        out item producedPayload subsets SystemOfInterest_Configuration::systemPayloads; 

        assign producedPayload.diagnosticsPacket := "producePayload_bb";

        then perform Operational_Behavior::evaluateQuality{
            assign initialQuality := 0.0;
            assign gainedQuality := initialQuality + qualityCapabilitySetting;
            comment producePayloadBbModelerComment about producePayload_bb locale "en_US"
            /* This is an exemplar assignment. */
        }
        bind evaluateQuality.gainedQuality = producedPayload.quality;
        bind evaluateQuality.qualityContribution = qualityCapabilitySetting;
    }

    action producePayload_gb subsets producePayload_bb {
        action buildPayload subsets Subsystem1_Behavior::buildPayload_gb;
        action verifyPayload subsets Subsystem2_Behavior::verifyPayload_bb;
    }
}