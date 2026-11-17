package Subsystem1_Behavior {
    
    action buildPayload_bb {
        in qualityCapabilitySetting defined by ScalarValues::Real;
        in item builtPayloadFeedback subsets Subsystem1_Configuration::subsystem1Feedbacks; 
        out item builtPayload subsets Subsystem1_Configuration::subsystem1Payloads;

        assign  builtPayload.diagnosticsPacket := "buildPayload_bb";

        then perform Operational_Behavior::evaluateQuality{
            assign initialQuality := 0.0;
            assign gainedQuality := initialQuality + qualityCapabilitySetting;
            comment buildPayloadBbModelerComment about buildPayload_bb locale "en_US"
            /* This is an exemplar assignment. */
        }
        bind evaluateQuality.gainedQuality = builtPayload.quality;
        bind evaluateQuality.qualityContribution = qualityCapabilitySetting;
    }

    action buildPayload_gb subsets buildPayload_bb {
        action prebuildPayload subsets Component1_1_Behavior::prebuildPayload_bb;
        action finishPayload subsets Component1_2_Behavior::finishPayload_bb;
    }

}