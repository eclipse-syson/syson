package Operational_Configuration {
    doc
    /* The Operational level 0 is described in this package
     * the Operational BlackBox defines the operational context of the system of interest at level 0 as a blackbox.
     * the GrayBox context decomposes the operational context in interacting systems and the system of interest
     * and performs the actions defined in the actionContext_gb. The actions exchange payloads
     * through the connection directly without ports.
     * the Operational WhiteBox redefines the parts of the GrayBox and subset them to the parts of the 
     * L1 GrayBox.
     * The connectors are subsetted to the ones of the L1 GrayBox and add the interface to enable the ports 
     * and flows between the parts.
     */

    item operationalPayloads subsets Operational_Taxonomy::operationalExchangeItems subsets MyStandard::serviceExchangeItems subsets MyStandard::serviceExchangeDiagnostics {
        doc
        /* The operational payload represents the data or material being exchanged in the operational context.
         * This operational payload extends the ontology exchange with the quality attribute that represents the 
         * gain of quality that the payload achieves through its life cycle in the operational context, this
         * being a particular type of value stream gain that is relevant in the operational context and
         * complying with the MyStandard.
         */
    }
    item operationalFeedbacks subsets Operational_Taxonomy::operationalExchangeItems {
        attribute feedback defined by ScalarValues::Real [1];
    }

    part operationalContext_bb subsets Operational_Taxonomy::myOperationPerformers [1] {
        doc
        /* The operational context is defined as a single performer that represents the whole context. 
         * It inherits the features of the operational performer in the ontology. It is a blackbox, so it is not decomposed 
         * and does not have any ports.
         */

        attribute redefines qualityContribution = exchangePayload_bb.payload.quality;
        attribute maintenanceStrain = 0.0;
        attribute strainPenaltyFactor = 0.1;
        attribute qualityContributionAdjusted = qualityContribution - (strainPenaltyFactor * maintenanceStrain);
        perform action exchangePayload_bb references Operational_Behavior::exchangePayload_bb;

        satisfy requirement payloadQualityGainOperationalReq subsets Operational_Requirements::payloadQualityGainOperational;
    }

    part operationalContext_gb subsets operationalContext_bb {
        comment operationalContextGbModelerComment about operationalContext_gb locale "en_US"
        /* Once we have a graybox the qualityGain will be defined by its constituents. */
        part mySystemOfInterest subsets SystemOfInterest_Configuration::systemOfInterest_bb;
        part myInteractingSystem subsets InteractingSystem_Configuration::interactingSystem_bb;

        perform action exchangePayload_gb
            redefines exchangePayload_bb references Operational_Behavior::exchangePayload_gb {
            
            comment S_Friedenthal /* an action at the context level may as well be described as a UseCase */
        }

        connection operationalConnections_gb [1] subsets Operational_Taxonomy::myOperationalConnectors {
            end connectingPerformer [1] redefines myConnectionSource references mySystemOfInterest;
            end connectedPerformer [1] redefines myConnectionTarget references myInteractingSystem;
            doc
            /* This connection inherits the attributes of the operational connectors in the taxonomy and 
             * connects the action parameters of the interacting parties without creating ports.
             */
            flow verifiedPayload subsets myOperationalFlow [1]
                from connectingPerformer.producePayload_bb.producedPayload
                to connectedPerformer.consumePayload_bb.producedPayload;
            flow feedback subsets myOperationalFlow [1]
                from connectedPerformer.consumePayload_bb.producedPayloadFeedback
                to connectingPerformer.producePayload_bb.producedPayloadFeedback;
        }

        satisfy requirement payloadQualityGainOperationalDerivedReq redefines payloadQualityGainOperationalReq subsets Operational_Requirements::payloadQualityGainOperationalDerived;
    }

    part operationalContext_wb subsets operationalContext_gb {
        part
            redefines mySystemOfInterest subsets SystemOfInterest_Configuration::systemOfInterest_wb {
            doc
            /* We can mount (integrate) the operationlContext by redefining it to the Whitebox
             * instead of the GrayBox owned parts if they are available for the fullest integration.
             */
            bind operationalConnections_wb.myOperationalInterface.supplierPort.operationalPayload =
                producePayload_gb.verifyPayload.verifiedPayload;
            bind operationalConnections_wb.myOperationalInterface.supplierPort.operationalFeedback =
                producePayload_gb.verifyPayload.verifiedPayloadFeedback;
        }
        comment myInteractingSystemModelerComment about myInteractingSystem locale "en_US"
        /* interactingSystem is not decomposed any further and is therefore
         * declared as a blackbox.
         */
        part
            redefines myInteractingSystem
            subsets InteractingSystem_Configuration::interactingSystem_bb {
            bind operationalConnections_wb.myOperationalInterface.consumerPort.operationalPayload =
                consumePayload_bb.producedPayload;
            bind operationalConnections_wb.myOperationalInterface.consumerPort.operationalFeedback =
                consumePayload_bb.producedPayloadFeedback;
        }
        comment exchangePayloadGbModelerComment about exchangePayload_gb locale "en_US"
        /* The system action at the whitebox level is an empty shell left for
         * traceability purposes. By redefining it, we redefine it empty, so
         * nothing can be executed.
         */
        perform action redefines exchangePayload_gb;

        comment operationalConnectionsWbModelerComment
            about operationalConnections_wb
            locale "en_US"
        /* The connection subsets the graybox connection and adds the interface,
         * enabling ports and flows between the parts. Flows are defined in the
         * interface definition in the taxonomy for this example, but they could
         * also be defined locally when multiple connections share the same
         * interface with different flows.
         */

        

        connection operationalConnections_wb subsets operationalConnections_gb [1] {
            end [1] redefines connectingPerformer references mySystemOfInterest.payloadVerifier;
            end [1] redefines connectedPerformer references myInteractingSystem;
            doc
            /* This connection tunnels through the systemOfInterest into the payloadVerifier.
             * The tunnel makes binding the ports between part and owned parts unnecessary.
             * The interface creates the ports at each side of the connection.
             * The connector is subsetting the graybox connection, so it inherits the flows and 
             * adds the interface to enable the ports and flows between the parts. The subsetting
             * allows to have one connector for each target tunneled part.
             */
            comment S_Friedenthal /* connectors need to be subsetted if they connect to more than one component*/
            
            interface myOperationalInterface defined by Operational_Taxonomy::MyOperationalInterface {
                flow verifiedPayload redefines myOperationalPayload;
                flow verifiedPayloadFeedback redefines myOperationalFeedback;
            }
        }
    }
}
