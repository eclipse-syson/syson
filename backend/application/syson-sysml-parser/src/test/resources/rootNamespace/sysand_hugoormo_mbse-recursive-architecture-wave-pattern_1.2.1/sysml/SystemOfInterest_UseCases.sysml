package SystemOfInterest_UseCases {

    part systemOfInterest_bb subsets SystemOfInterest_Configuration::systemOfInterest_bb {
        use case 'the payload is first produced and then verified' {
            doc
            /* This use case drafts the behavior of the involved parts 
             * within the system of interest
             */

            objective {
                doc
                /* A payload will be produced at one builder subsystem
                 * then the payload will be verified by one verification subsystem. The verifier
                 * will provide a feedback to the builder in exchange.
                 */
            }

            subject payloadBuilder [1] {
                event occurrence payloadBuiltSubmission;
                event occurrence verificationFeedbackReceived;
            }
            actor payloadVerifier [1] {
                event occurrence payloadBuiltReceived;
                event occurrence verificationFeedbackSubmitted;
            }

            message builtPayload
                from payloadBuilder.payloadBuiltSubmission to payloadVerifier.payloadBuiltReceived;
            then message verificationFeedback
                from payloadVerifier.verificationFeedbackSubmitted
                to payloadBuilder.verificationFeedbackReceived;
        }
    }

    part systemOfInterest_gb
        subsets systemOfInterest_bb, SystemOfInterest_Configuration::systemOfInterest_gb {
        use case redefines 'the payload is first produced and then verified' {
            doc
            /* The use case is redefined and the elements of the use case are
             * referenced to the realized elements in the gray box.
             */

            subject redefines payloadBuilder [1] references payloadBuilder;
            actor redefines payloadVerifier [1] references payloadVerifier;

            message redefines builtPayload references systemConnections_gb.builtPayload;
            then message
                redefines verificationFeedback references systemConnections_gb.verificationFeedback;
        }
    }
}
