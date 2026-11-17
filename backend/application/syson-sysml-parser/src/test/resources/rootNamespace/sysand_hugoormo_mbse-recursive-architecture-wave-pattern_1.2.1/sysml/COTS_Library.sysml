package MyCOTSLibrary {
    doc
    /* Reusable commercial off-the-shelf product library for the Recursive MBSE
     * Architecture Weave Pattern.
     *
     * Each COTS product shall be represented by its own package. That product
     * package bundles the supplier-owned exchange items, ports, interfaces,
     * product part definition, integration connector definitions, capability
     * claims, and evidence references. Project architectures select and
     * integrate these elements; they do not redesign their fixed COTS boundaries.
     */

    package PayloadVerifierCOTSProductPackage {
        doc
        /* Reusable library package for the reference payload verifier COTS product.
         * The package intentionally owns the product's exchange items, port,
         * interface, product definition, and integration connector together so that
         * practitioners do not treat the COTS library as a loose interface dump.
         */

        abstract item payloadVerifierCOTSExchangeItems subsets MySystemOntology::systemExchangeItems [0..*] {
            doc
            /* Exchange items accepted or emitted by the reference payload verifier COTS
             * interface. Concrete projects should specialize this item set with the
             * selected product's actual message, file, signal, or service data shape.
             */
        }

        abstract port def PayloadVerifierCOTSPort specializes MySystemOntology::SystemPort {
            doc
            /* Fixed supplier-owned service port exposed by the payload verifier COTS item.
             * The architecture may constrain compatibility with this port, but does not
             * claim ownership of its implementation.
             */

            in item COTSPayloadInput subsets payloadVerifierCOTSExchangeItems redefines systemInput [0..*];
            out item COTSVerificationFeedback subsets payloadVerifierCOTSExchangeItems redefines systemOutput [0..*];
        }

        abstract interface def PayloadVerifierCOTSInterface {
            doc
            /* Vendor-imposed interface contract for the reference payload verifier COTS.
             * This interface is part of the product specification and constrains what
             * can flow, in which direction, and under which product or technology limits.
             */

            end port designedEndpointPort : ~PayloadVerifierCOTSPort [1];
            end port COTSVerifierPort : PayloadVerifierCOTSPort [1];
            flow COTSPayloadInputFlow subsets payloadVerifierCOTSExchangeItems [0..*]
                from designedEndpointPort.COTSPayloadInput to COTSVerifierPort.COTSPayloadInput;
            flow COTSVerificationFeedbackFlow subsets payloadVerifierCOTSExchangeItems [0..*]
                from COTSVerifierPort.COTSVerificationFeedback to designedEndpointPort.COTSVerificationFeedback;
        }

        abstract part payloadVerifierCOTSProduct subsets MySystemOntology::systems [0..*] {
            doc
            /* Reusable library entry for a selected payload verifier COTS product.
             * The product is supplier-owned and terminal from the viewpoint of this
             * reference architecture. The project model may select and integrate it,
             * but should not decompose it as if it were internally designed.
             */

            port verifierServicePort : PayloadVerifierCOTSPort [1];
            attribute vendorIdentifier : ScalarValues::String = "ReferencePayloadVerifierVendor";
            attribute productIdentifier : ScalarValues::String = "ReferencePayloadVerifierCOTS";
            attribute supplierEvidenceReference : ScalarValues::String = "Vendor datasheet, ICD, and qualification evidence";
            attribute lifecycleRiskNote : ScalarValues::String = "Check obsolescence, licensing, certification, and support constraints";
        }

        abstract connection def PayloadVerifierCOTSIntegrationConnector
            specializes MyFramework::MyFrameworkConfigurationBehaviorSemantics::COTSIntegrationConnector,
                MySystemOntology::SystemConnector {
            doc
            /* Reusable connector category for integrating the payload verifier COTS.
             * Project-level connectors may subset this connector when the selected COTS
             * imposes the verifier interface on the designed endpoint. The interface usage
             * below is the explicit compatibility constraint; the selected COTS product is
             * identified by the endpoint usage that subsets payloadVerifierCOTSProduct.
             */

            interface payloadVerifierCOTSInterface defined by PayloadVerifierCOTSInterface;
        }

        abstract connection payloadVerifierCOTSIntegrations
            defined by PayloadVerifierCOTSIntegrationConnector [0..*] nonunique;
    }
}
