package Component1_1_Behavior {
    action prebuildPayload_bb {
        in qualityCapabilitySetting defined by ScalarValues::Real;
        in item prebuiltPayloadFeedback subsets Component1_1_Configuration::component1_1Feedbacks;
        out item prebuiltPayload subsets Component1_1_Configuration::component1_1Payloads; 

        assign prebuiltPayload.diagnosticsPacket := "prebuildPayload_bb";

        then perform Operational_Behavior::evaluateQuality{
            in redefines initialQuality;
            out redefines gainedQuality;

            assign initialQuality := 0.0;
            assign gainedQuality := initialQuality + qualityCapabilitySetting;
        }

        bind evaluateQuality.gainedQuality = prebuiltPayload.quality;
        bind evaluateQuality.qualityContribution = qualityCapabilitySetting;
    }
}
