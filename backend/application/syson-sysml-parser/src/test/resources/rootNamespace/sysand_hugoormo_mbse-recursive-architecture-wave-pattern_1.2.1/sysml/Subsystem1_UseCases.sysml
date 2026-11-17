package Subsystem1_UseCases {

    part payloadBuilder_bb subsets Subsystem1_Configuration::payloadBuilder_bb {
        use case 'the payload is first prebuilt and then finished' {
            doc
            /* This use case drafts the behavior of the involved parts 
             * withih the payloadBuilder
             */

            objective {
                doc
                /* A payload will be prebuilt at one prebuilder subsystem
                 * then the prebuilt payload will be finished by one payloadFinisher subsystem. 
                 * The payloadFinisher will provide a feedback to the builder in exchange.
                 */
            }

            subject preBuilder [1] {
                event occurrence preBuiltPayloadSubmission;
                event occurrence finishingFeedbackReceived;
            }
            actor finisher [1] {
                event occurrence preBuiltPayloadReceived;
                event occurrence finishingFeedbackSubmitted;
            }

            message preBuiltPayload
                from preBuilder.preBuiltPayloadSubmission to finisher.preBuiltPayloadReceived;
            then message finishedFeedback
                from finisher.finishingFeedbackSubmitted
                to preBuilder.finishingFeedbackReceived;
        }
    }

    part payloadBuilder_gb
        subsets payloadBuilder_bb, Subsystem1_Configuration::payloadBuilder_gb {
        use case redefines 'the payload is first prebuilt and then finished' {
            doc
            /* The use case is redefined and the elements of the use case are
             * referenced to the realized elements in the gray box.
             */

            subject redefines preBuilder [1] references payloadPrebuilder;
            actor redefines finisher [1] references payloadFinisher;

            message redefines preBuiltPayload references system1Connections_gb.prebuiltPayload;
            then message
                redefines finishedFeedback references system1Connections_gb.finishedFeedback;
        }
    }
}
