package InteractingSystem_Behavior {
    
    action consumePayload_bb {
        in qualityCapabilitySetting defined by ScalarValues::Real;
        in item producedPayload subsets InteractingSystem_Configuration::myInteractingSystemPayloads;   
        out item producedPayloadFeedback subsets InteractingSystem_Configuration::myInteractingSystemFeedbacks;
        out item consumedPayload subsets InteractingSystem_Configuration::myInteractingSystemPayloads; 

        assign consumedPayload.diagnosticsPacket := "consumePayload_bb";

        then perform Operational_Behavior::evaluateQuality{
            assign initialQuality := 0.0;
            assign gainedQuality := initialQuality + qualityCapabilitySetting;
            comment consumePayloadBbModelerComment about consumePayload_bb locale "en_US"
            /* This is an exemplar assignment. */
        }

        bind evaluateQuality.initialQuality = producedPayload.quality;
        bind evaluateQuality.gainedQuality = consumedPayload.quality;
        bind evaluateQuality.qualityContribution = qualityCapabilitySetting;
    }
}
