package MyOperationalOntology {
    doc
    /* The Operational Ontology defines the concepts and relationships that are used at the operational level.
     * The elements of the ontology are abstract and intended to be specialized or subsetted.
     * Ontologies may become additive and build on top of each other, from the most generic to the most domain specific.
     * This exemplar ontology is illustrative and is inspired by the UAF. It is not intended to 
     * be a comprehensive ontology. In an implementation of this architecture pattern, the formal UAF
     * ontology ought to be utilized, or a specialization thereof.
     */

    abstract item def OperationalExchangeItem {
        doc
        /* The operational exchange item represents and abstraction of the data or material being exchanged 
         * in the operational context. Usages must be specialized or subsetted.
         */
    }

    abstract item operationalExchangeItems defined by OperationalExchangeItem [0..*] nonunique;

    abstract connection def OperationalConnector {
        end connectionSource defined by OperationalPerformer redefines Connections::BinaryConnection::source;
        end connectionTarget defined by OperationalPerformer redefines Connections::BinaryConnection::target;
        flow operationalFlow defined by OperationalExchangeItem [0..*]{
            end part connectionSource defined by OperationalPerformer;
            end part connectionTarget defined by OperationalPerformer;  
        }
    }

    abstract connection operationalConnectors defined by OperationalConnector [0..*] nonunique;

    abstract action def OperationalAction {
        doc
        /* An action in the operational context */
        in abstract item inputParameter defined by OperationalExchangeItem [0..*];
        out abstract item outputParameter defined by OperationalExchangeItem [0..*];

        action operationalSubactions defined by OperationalAction [0..*];
    }

    abstract action operationalActions defined by OperationalAction [0..*] nonunique;

    abstract part def OperationalPerformer {
        doc
        /* This part definition represents an operational performer, which is a concept that represents 
         * the performers that participate in the operational context.
         */
        part operationalPerformerParts subsets operationalPerformers [0..*];
        port operationalPorts subsets operationalPorts [0..*] redefines ownedPorts;
    }

    abstract part operationalPerformers defined by OperationalPerformer [0..*] nonunique {
        doc
        /*  */
    }

    abstract port def OperationalPort {
        doc
        /* Defines features of operational ports */
        in item operationalInput defined by OperationalExchangeItem [0..*];
        out item operationalOutput defined by OperationalExchangeItem [0..*];
    }

    abstract port operationalPorts : OperationalPort [0..*] nonunique {
        doc
        /*  */
    }
    comment myOperationalOntologyModelerComment about MyOperationalOntology locale "en_US"
    /* End of package MyOperationalOntology. */
}

package MySystemOntology {
    doc
    /* The System Ontology defines the concepts and relationships that are used in the definition of systems at 
     * level 1 and beyond.
     * This exemplar ontology is illustrative and is not intended to be a comprehensive ontology.
     */

    abstract item def SystemExchangeItem {
        doc
        /* The system exchange item represents the data or material being exchanged in and between systems. */
    }

    abstract item systemExchangeItems defined by SystemExchangeItem [0..*] nonunique;

    abstract connection def SystemConnector {
        end connectionSource defined by System redefines Connections::BinaryConnection::source;
        end connectionTarget defined by System redefines Connections::BinaryConnection::target;
        flow systemFlow defined by SystemExchangeItem [0..*]{
            end part connectionSource defined by System;
            end part connectionTarget defined by System;  
        }
    }

    abstract connection systemConnectors defined by SystemConnector [0..*] nonunique;

    abstract action def SystemAction {
        doc
        /* An action in the system context */
        in item inputParameter defined by SystemExchangeItem [0..*];
        out item outputParameter defined by SystemExchangeItem [0..*];

        action systemSubactions defined by SystemAction [0..*];
    }

    abstract action systemActions defined by SystemAction [0..*] nonunique;

    abstract part def System {
        doc
        /* This part definition represents a system element, which is a concept that represents 
         * the elements that compose a system.
         */
        part systemParts subsets systems [0..*];
        port systemPorts subsets systemPorts [0..*] redefines ownedPorts;
    }

    abstract part systems defined by System [0..*] nonunique;

    abstract port def SystemPort {
        doc
        /* Defines features of system ports */
        in item systemInput defined by SystemExchangeItem [0..*];
        out item systemOutput defined by SystemExchangeItem [0..*];
    }

    abstract part systemPorts : SystemPort [0..*] nonunique {
        doc
        /*  */
    }
}
