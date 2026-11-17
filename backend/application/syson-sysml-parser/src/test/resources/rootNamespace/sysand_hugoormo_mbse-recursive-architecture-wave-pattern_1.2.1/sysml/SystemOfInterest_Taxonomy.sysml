package SystemOfInterest_Taxonomy {
    doc
    /* items, parts, ports, interfaces, and connections definitions in the 
     * context of the system of interest.
     * At this level it may be relevant to inherit aspects of both the operational and system ontologies, as the interacting system 
     * is a system that participates in the operational context and interacts with the system of interest.
     * This is a local product taxonomy for the system-of-interest owner. Names may differ
     * from other level taxonomies by design; subsetting and specialization preserve the
     * semantic bridge to shared ontology and standard concepts.
     * See MyFramework::MyFrameworkFoundationSemantics::localTaxonomyOwnershipRule.
     */

    abstract item mySystemExchangeItems subsets MySystemOntology::systemExchangeItems [0..*];

    abstract part mySystems subsets MySystemOntology::systems [0..*] {
        attribute qualityContribution defined by ScalarValues::Real;
    }
    
    abstract connection def MySystemOfInterestConnection specializes MySystemOntology::SystemConnector {
        doc
        /* The system connection represents the interaction between the system of interest and the interacting system in the operational context. 
         * It may inherit features from the ontology connector and add features that are relevant for the target system of interest context.
         */
        end myConnectionSource redefines connectionSource;
        end myConnectionTarget redefines connectionTarget;
        flow mySystemFlow redefines systemFlow [0..*];
    }

    abstract connection mySystemOfInterestConnections defined by MySystemOfInterestConnection [0..*] nonunique;
    
    abstract port def MySystemOfInterestPort specializes MySystemOntology::SystemPort {
        doc
        /* The system port is a specialization of the operational port that defines the payload and feedback
         * that are relevant to the system of interest. It may be further specialized in the L1 context.
         */
        in item mySystemInput subsets mySystemExchangeItems redefines systemInput [0..*]; 
        out item mySystemOutput subsets mySystemExchangeItems redefines systemOutput [0..*];
    }
    abstract interface def MySystemInterface {
        end port mySupplierPort defined by MySystemOfInterestPort [1];
        end port myConsumerPort defined by ~MySystemOfInterestPort [1];
        flow mySystemFlow subsets mySystemExchangeItems [0..*]
            from mySupplierPort.mySystemOutput to myConsumerPort.mySystemOutput;
        flow mySystemReturnFlow subsets mySystemExchangeItems [0..*]
            from myConsumerPort.mySystemInput to mySupplierPort.mySystemInput;
    }

    abstract item mySystemExchangeDataPacket subsets MySystemOntology::systemExchangeItems subsets MyFramework::MyFrameworkStackInterface::dataPacket [0..*];

    abstract interface def MySystemStackInterface  {
        doc /* By way of example we want to specify an application protocol connection within the system-of-interest
            * using the stack ports provisioned by myFramework.
            */
        end port mySupplierStackPort defined by MyFramework::MyFrameworkStackInterface::MyStackPort;
        end port myConsumerStackPort defined by ~MyFramework::MyFrameworkStackInterface::MyStackPort;
        flow mySystemStackAppFlow subsets mySystemExchangeDataPacket [0..*]
            from mySupplierStackPort.applicationProtocolPort.applicationPackageOut to myConsumerStackPort.applicationProtocolPort.applicationPackageIn;
        comment mySystemStackAppFlowModelerComment about mySystemStackAppFlow locale "en_US"
        /* Consumer end is conjugated, so this target feature is
         * direction-compatible at the connector boundary.
         */
        flow mySystemStackAppReturnFlow subsets mySystemExchangeDataPacket [0..*]
            from myConsumerStackPort.applicationProtocolPort.applicationPackageOut to mySupplierStackPort.applicationProtocolPort.applicationPackageIn;
        comment mySystemStackAppReturnFlowModelerComment about mySystemStackAppReturnFlow locale "en_US"
        /* Names stay the same under conjugation; effective end-direction is
         * inverted on the consumer side.
         */
    }
}
