package Operational_Taxonomy {
    doc
    /* items, parts, ports, interfaces, and connections definitions in the 
     * operational context that are relevant for the definition of the operational context at level 0 and beyond.
     * The taxonomy may specialize elements of an ontology to contextualize them for the specific characteristics of the target operational context. 
     * The taxonomy may also define new elements that are not defined in the ontology but are relevant for the target operational context.
     * Repeated local taxonomies are intentional in the recursive pattern: each level or
     * organizational entity may use its own vocabulary, while subsetting and specialization
     * maintain traceability to shared ontology and standard semantics.
     * See MyFramework::MyFrameworkFoundationSemantics::localTaxonomyOwnershipRule.
     */

    abstract item def MyOperationalExchangeItem specializes MyOperationalOntology::OperationalExchangeItem {
        doc
        /* The MyOperationalExchangeItem represents the operational exchange items in the example operational context.
         * It specializes the operational exchange item defined in the ontology and adds the attributes that
         * are specific of the operational environment.
         */
    }

    abstract item operationalExchangeItems defined by MyOperationalExchangeItem [0..*] nonunique {
        doc
        /* The operational exchange items in the example operational context. */
    }

    abstract part def MyOperationPerformer specializes MyOperationalOntology::OperationalPerformer {
        doc /* The operational performers contribute with a specific quality gain. */
        attribute qualityContribution defined by ScalarValues::Real;
    }
    
    abstract part myOperationPerformers defined by MyOperationPerformer [0..*] nonunique {
        doc /* The operational performers contribute with a specific quality gain. */
    }

    abstract connection def MyOperationalConnector specializes MyOperationalOntology::OperationalConnector {
        doc
        /* The operational connector represents the interaction between two performers in the operational context. 
         * It may inherit features from the ontology connector and add features that are relevant for the target operational context.
         */
        end myConnectionSource redefines connectionSource;
        end myConnectionTarget redefines connectionTarget;
        flow myOperationalFlow redefines operationalFlow;
    }

    abstract connection myOperationalConnectors defined by MyOperationalConnector [0..*] nonunique;

    abstract port def MyOperationalPort specializes MyOperationalOntology::OperationalPort {
        doc
        /* The example operational port represents the interaction point of a performer in the example operational context.
         * It defines the features of the ports through which the performers exchange payloads in the example operational context.
         * It inherits the features of the ontology operational port and adds the features of the payload and feedback 
         * that are exchanged through the port.
         */
        in item operationalFeedback subsets operationalExchangeItems [1] redefines operationalInput;
        out item operationalPayload subsets operationalExchangeItems [1] redefines operationalOutput;
    }

    abstract interface def MyOperationalInterface {
        comment myOperationalInterfaceModelerComment about MyOperationalInterface locale "en_US"
        /* Ports may not be subsetted by a conjugated port (tool/specification limitation). */
        end port supplierPort defined by MyOperationalPort [1];
        end port consumerPort defined by ~MyOperationalPort [1];
        flow myOperationalPayload subsets operationalExchangeItems [1]
            from supplierPort.operationalPayload to consumerPort.operationalPayload;
        flow myOperationalFeedback subsets operationalExchangeItems [1]
            from consumerPort.operationalFeedback to supplierPort.operationalFeedback;
    }
}
