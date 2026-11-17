package Subsystem2_Behavior {
    action verifyPayload_bb {
        in qualityCapabilitySetting defined by ScalarValues::Real;
        in item verifiedPayloadFeedback subsets Subsystem2_Configuration::subsystem2Feedbacks;
        in item builtPayload subsets Subsystem2_Configuration::subsystem2Payloads; 
        out item verifiedPayload subsets Subsystem2_Configuration::subsystem2Payloads; 
        out item builtPayloadFeedback subsets Subsystem2_Configuration::subsystem2Feedbacks;

        perform Operational_Behavior::evaluateQuality{

            assign gainedQuality := initialQuality + qualityCapabilitySetting;
            comment verifyPayloadBbModelerComment about verifyPayload_bb locale "en_US"
            /* This is an exemplar assignment. */
        }
        bind evaluateQuality.initialQuality = builtPayload.quality;
        bind evaluateQuality.gainedQuality = verifiedPayload.quality;
        bind evaluateQuality.qualityContribution = qualityCapabilitySetting;
    }
}
