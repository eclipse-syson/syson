package Component1_2_Behavior {
    
    action finishPayload_bb {
        in qualityCapabilitySetting defined by ScalarValues::Real;
        in item finishedPayloadFeedback subsets Component1_2_Configuration::component1_2Feedbacks;
        in item prebuiltPayload subsets Component1_2_Configuration::component1_2Payloads; 
        out item finishedPayload subsets Component1_2_Configuration::component1_2Payloads; 
        out item prebuiltPayloadFeedback subsets Component1_2_Configuration::component1_2Feedbacks;

        perform Operational_Behavior::evaluateQuality{
            in redefines initialQuality;
            out redefines gainedQuality;

            assign gainedQuality := initialQuality + qualityCapabilitySetting;
        }
        bind evaluateQuality.initialQuality = prebuiltPayload.quality;
        bind evaluateQuality.gainedQuality = finishedPayload.quality;
        bind evaluateQuality.qualityContribution = qualityCapabilitySetting;
    }

}