package SystemOfInterest_Configuration {
    doc
    /* The system of interest is described in this package
     * the BlackBox system of interest defines the system of interest as a blackbox.
     * the GrayBox system of interest decomposes the system of interest in subsystems 
     * and performs the actions defined in the actionL1_gb. The actions exchange payloads
     * through the connection directly without ports.
     * the WhiteBox system of interest redefines the parts of the GrayBox and subset them 
     * to the graybox subsystems.
     */

    item systemPayloads
        subsets
            SystemOfInterest_Taxonomy::mySystemExchangeItems,
            Operational_Configuration::operationalPayloads;
    item systemFeedbacks
        subsets
            SystemOfInterest_Taxonomy::mySystemExchangeItems,
            Operational_Configuration::operationalFeedbacks;
    comment systemExchangeCompatibilityModelerComment about systemPayloads locale "en_US"
    /* Enforce compatibility of the system exchange items with the operational exchange items. */

    part systemOfInterest_bb
        subsets MyStandard::myStandardComplyingPart [1]
        subsets SystemOfInterest_Taxonomy::mySystems {
        doc
        /* The blackbox system of interest represents the system of interest as a blackbox,
         * by way of example compliant with the quality gain defined by the MyStandard.
         * The requirements satisfaction against the MyStandard is inherited from the 
         * myStandardComplyingPart.
         */
        comment systemOfInterestBbModelerComment about systemOfInterest_bb locale "en_US"
        /* Original requirement: realized contribution comes from behavioral output,
         * while capability is provided as an independent seeded input.
         */
        attribute qualityCapabilitySetting defined by ScalarValues::Real [1]
            default
                SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadBuilderGain
                + SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived.systemPayloadThroughPayloadVerifierGain;
        attribute realizedQualityContribution = producePayload_bb.producedPayload.quality;
        attribute redefines qualityContribution = realizedQualityContribution;
        attribute maintenanceStrain = 0.0;
        attribute strainPenaltyFactor = 0.1;
        attribute qualityContributionAdjusted =
            qualityContribution - strainPenaltyFactor * maintenanceStrain;
        perform action producePayload_bb references SystemOfInterest_Behavior::producePayload_bb {
            in qualityCapabilitySetting = systemOfInterest_bb.qualityCapabilitySetting;
        }

        satisfy requirement payloadQualityGainSystemOfInterestReq
            subsets SystemOfInterest_Requirements::payloadQualityGainSystemOfInterest;
    }

    part systemOfInterest_gb subsets systemOfInterest_bb {
        comment systemOfInterestGbModelerComment about systemOfInterest_gb locale "en_US"
        /* Derived requirement: qualityContribution is implicitly sum of constituents;
         * validated by requirement constraint. Once we have a graybox, the
         * qualityGain will be defined by its constituents.
         */
        part payloadBuilder subsets Subsystem1_Configuration::payloadBuilder_bb;
        part payloadVerifier subsets Subsystem2_Configuration::payloadVerifier_bb;

        perform action producePayload_gb
            redefines systemOfInterest_bb.producePayload_bb
            references SystemOfInterest_Behavior::producePayload_gb;

        connection systemConnections_gb
            subsets SystemOfInterest_Taxonomy::mySystemOfInterestConnections [1]
            subsets
                MyCOTSLibrary::PayloadVerifierCOTSProductPackage::payloadVerifierCOTSIntegrations {
            end connectingSystem redefines myConnectionSource [1] references payloadBuilder;
            end connectedSystem redefines myConnectionTarget [1] references payloadVerifier;
            doc
            /* This rich connector connects the action parameters of the interacting parties.
             * Because the payload verifier is selected as a COTS item, the connector also
             * incorporates the supplier-imposed interface contract already at graybox level.
             * This is a compatibility assertion on the designed payloadBuilder endpoint,
             * not an implementation of the COTS port by the connector.
             * See MyFramework::MyFrameworkConfigurationBehaviorSemantics::COTSConnectorBoundaryRule.
             */
            flow builtPayload subsets mySystemFlow [1]
                from connectingSystem.buildPayload_bb.builtPayload
                to connectedSystem.verifyPayload_bb.builtPayload;
            flow verificationFeedback subsets mySystemFlow [1]
                from connectedSystem.verifyPayload_bb.builtPayloadFeedback
                to connectingSystem.buildPayload_bb.builtPayloadFeedback;
        }

        satisfy requirement payloadQualityGainSystemOfInterestDerivedReq
            subsets SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived;
    }

    part systemOfInterest_wb subsets systemOfInterest_gb {
        doc
        /* Possible antipattern: subsystem may inherit features from the 
         * redefinition and from the subsetting, that may be meant to be the same 
         * features
         */

        part redefines payloadBuilder subsets Subsystem1_Configuration::payloadBuilder_gb {
            bind systemConnections_wb.systemOfInterestStackInterface.mySupplierStackPort
                            .applicationProtocolPort.applicationPackageOut =
                buildPayload_gb.finishPayload.finishedPayload;
            bind systemConnections_wb.systemOfInterestStackInterface.mySupplierStackPort
                            .applicationProtocolPort.applicationPackageIn =
                buildPayload_gb.finishPayload.finishedPayloadFeedback;
        }
        comment payloadVerifierModelerComment about payloadVerifier locale "en_US"
        /* payloadVerifier is not decomposed any further and is therefore
         * declared as a blackbox.
         */
        part redefines payloadVerifier subsets Subsystem2_Configuration::payloadVerifier_bb {
            bind systemConnections_wb.systemOfInterestStackInterface.myConsumerStackPort
                            .applicationProtocolPort.applicationPackageOut = verifyPayload_bb.builtPayloadFeedback;
            bind systemConnections_wb.systemOfInterestStackInterface.myConsumerStackPort
                            .applicationProtocolPort.applicationPackageIn = verifyPayload_bb.builtPayload;
        }

        perform action redefines producePayload_gb;

        connection systemConnections_wb subsets systemConnections_gb [1] {
            end redefines connectingSystem [1] references payloadBuilder.payloadFinisher;
            end redefines connectedSystem [1] references payloadVerifier;
            doc
            /* This connection tunnels through the payloadBuilder into the payloadFinisher.
             * The tunnel makes binding the ports between part and owned parts unnecessary.
             * The interface creates the ports at each side of the connection.
             */
            comment S_Friedenthal
            /* connectors need to be subsetted if they connect to more than one component */

            interface systemOfInterestStackInterface
                defined by SystemOfInterest_Taxonomy::MySystemStackInterface {
                doc
                /* This interface defines the stack-level application protocol ports for the system of interest, 
                * allowing for the tunneling of connections through the payloadBuilder into the payloadFinisher. 
                */
                flow builtPayload redefines mySystemStackAppFlow;
                flow verificationFeedback redefines mySystemStackAppReturnFlow;
            }
        }
    }
comment systemOfInterestConfigurationModelerComment about SystemOfInterest_Configuration locale "en_US"
/* End of package SystemOfInterest_Configuration. */
}
