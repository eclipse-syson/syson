package Subsystem1_Taxonomy {
    doc
    /* items, parts, ports, interfaces, and connections definitions in the 
     * context of the subsystem1.
     * This local taxonomy represents the payload-builder owner's vocabulary and
     * maps it back to the shared system ontology.
     * Local naming differences are intentional and model organizational ownership of
     * product taxonomies. See
     * MyFramework::MyFrameworkFoundationSemantics::localTaxonomyOwnershipRule.
     */

    abstract item mySubsystem1ExchangeItems subsets MySystemOntology::systemExchangeItems [0..*];

    abstract part mySubsystems1 subsets MySystemOntology::systems [0..*] {
        attribute qualityContribution defined by ScalarValues::Real;
    }

    abstract connection def MySubsystem1Connection specializes MySystemOntology::SystemConnector {
        doc
        /* The system connection represents the interaction between the system of interest and the interacting system in the operational context. 
         * It may inherit features from the ontology connector and add features that are relevant for the target system of interest context.
         */
        end myConnectionSource redefines connectionSource;
        end myConnectionTarget redefines connectionTarget;
        flow mySystemFlow redefines systemFlow [0..*];
    }
    abstract connection mySubsystem1Connections defined by MySubsystem1Connection [0..*] nonunique;
    
    abstract port def MySubsystem1Port specializes SystemOfInterest_Taxonomy::MySystemOfInterestPort {
        doc
        /* The subsystem port is a specialization of the system port that defines the payload and feedback
         * that are relevant to the subsystem. It may be further specialized in the L2 context.
         */
        in item subsystem1Feedback subsets mySubsystem1ExchangeItems redefines mySystemInput [0..*];
        out item subsystem1Payload subsets mySubsystem1ExchangeItems redefines mySystemOutput [0..*];
    }
    abstract interface def MySubsystem1Interface {
        end port supplierPort : MySubsystem1Port [1];
        end port consumerPort : ~MySubsystem1Port [1];
        flow subsystem1Payload subsets mySubsystem1ExchangeItems [0..*]
            from supplierPort.subsystem1Payload to consumerPort.subsystem1Payload;
        flow subsystem1Feedback subsets mySubsystem1ExchangeItems [0..*]
            from consumerPort.subsystem1Feedback to supplierPort.subsystem1Feedback;
    }
}
