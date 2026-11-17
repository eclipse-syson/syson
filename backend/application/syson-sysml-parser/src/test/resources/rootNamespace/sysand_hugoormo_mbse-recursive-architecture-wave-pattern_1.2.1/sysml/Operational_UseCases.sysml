package Operational_UseCases {
    doc
    /* The use cases assist in defining the behavior at each system level. 
     * For each use of the subject a use case is defined with its messages.
     * The messages defined in the use cases must be later, in the realization of the 
     * use case, created by some part. The messages must also be processed by some part.
     * Creating and processing messages is realized in actions.
     * the traceability between the preliminary work in the use case and the realization
     * in the actions is established by referencing the use case messages to the flows
     * at the GrayBox.
     */

    part context_bb subsets Operational_Configuration::operationalContext_bb {
        use case 'exchange payload between SoI and interacting system' {
            doc
            /* This use case drafts the behavior of the involved parts 
             * within the context
             */

            objective {
                doc
                /* A payload will be produced at one payloadProducer
                 * that will be consumed by one payloadConsumer. The payloadConsumer
                 * will provide a feedback to the payloadProducer in exchange.
                 */
            }

            subject payloadProducer [1] {
                event occurrence payloadSubmission;
                event occurrence feedbackReceived;
            }
            actor payloadConsumer [1] {
                event occurrence payloadReceived;
                event occurrence feedbackSubmitted;
            }

            message verifiedPayload
                from payloadProducer.payloadSubmission to payloadConsumer.payloadReceived;
            then message consumedPayload
                from payloadConsumer.feedbackSubmitted to payloadProducer.feedbackReceived;
        }
    }

    part context_gb subsets context_bb, Operational_Configuration::operationalContext_gb {
        use case redefines 'exchange payload between SoI and interacting system' {
            doc
            /* The use case is redefined and the elements of the use case are
             * referenced to the realized elements in the gray box.
             */

            subject redefines payloadProducer [1] references mySystemOfInterest;
            actor redefines payloadConsumer [1] references myInteractingSystem;

            message redefines verifiedPayload references operationalConnections_gb.verifiedPayload;
            then message redefines consumedPayload references operationalConnections_gb.feedback;
        }
    }
}
