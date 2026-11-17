package MyStandard {
    doc
    /* This package defines an exemplar standard for the purpose of demonstrating the integration 
     * of a machine-readable standard in the MBSE RecursiveArchitecture Weave Pattern.
     * A standard is represented by way of example as a set of generic structural elements and constraints 
     * applicable to those elements.
     * Machine-readable standards shall be modeled as such that items or parts satisfy constraints
     * that are defined in the standard. This allows for the verification of compliance of items or parts
     * with the standard by checking whether the constraints are satisfied. Constraints with predicates
     * can be checked automatically, whether textual ones must be checked manually.
     *
     * In this reference pattern the standard is a compliance overlay. Local taxonomies
     * may use their own product terminology, while compliant architecture elements
     * subset MyStandard::myStandardComplyingPart when the standard applies.
     * See MyFramework::MyFrameworkFoundationSemantics::standardComplianceOverlayRule
     * and MyFramework::MyFrameworkFoundationSemantics::qualityVocabularyRule.
     */
    
    comment standardCompliantElementModelerComment about MyStandard locale "en_US"
    /* Items and parts that represent a compliant element. */

    abstract item serviceExchangeItems {
        doc
        /* The service exchange item represents the data or material being exchanged in a service exchange. */
        attribute quality : ScalarValues::Real;
    }

    abstract item serviceExchangeDiagnostics {
        doc
        /* The service exchange diagnostics represents the data or material being exchanged in a service exchange 
        * for diagnostic purposes. The diagnosticsPacket should be extended to contain the relevant information for 
        * diagnosing the service exchange, such as timestamps, identifiers of the exchanging parties, and any error 
        * messages or codes that may be relevant for understanding the quality gain achieved in the service exchange.
        */
        attribute diagnosticsPacket : ScalarValues::String;
        attribute prebuildExecuted : ScalarValues::Boolean = false;
        attribute finishExecuted : ScalarValues::Boolean = false;
        attribute verifyExecuted : ScalarValues::Boolean = false;
        attribute consumeExecuted : ScalarValues::Boolean = false;
    }

    abstract part partInScopeOfStandard {
        doc
        /* This part represents an element that is in the scope of the standard. It can be specialized to represent 
        * specific elements in different contexts that are relevant for the standard. */

        item serviceExchangeItem subsets serviceExchangeItems;
        item serviceExchangeDiagnostic subsets serviceExchangeDiagnostics;
        attribute qualityGain : ScalarValues::Real default 0.0;
    }

    abstract part myStandardComplyingPart subsets partInScopeOfStandard {
        doc
        /* This part represents an element that is compliant with the standard. It can be specialized to represent 
        *specific compliant elements in different contexts. */

        satisfy qualityGainRequirementOfComplyingElement;
    
    }
    comment standardRequirementsModelerComment about qualityGainRequirementOfComplyingElement locale "en_US"
    /* Requirements consolidate sets of constraints that ought to be complied with. */

    requirement qualityGainRequirementOfComplyingElement {
        doc
        /* The quality gain requirement specifies the required quality gain for an operational element to be 
        * compliant with the standard. */

        subject standardComplyingPart subsets partInScopeOfStandard;
        require qualityGainStandardTypeA {
            redefines serviceExchangeItem = standardComplyingPart.qualityGain;
        }
    }

    comment reusableConstraintsModelerComment about qualityGainStandardTypeA locale "en_US"
    /* Constraints can be reused in multiple requirements. */

    constraint qualityGainStandardTypeA {
        doc
        /* The quality gain of an exchange of services in an operational ensemble shall be at least
         * 9 quality units to ensure institutional effectivity.
         */
        
        attribute serviceExchangeItem defined by ScalarValues::Real [1];
            
        serviceExchangeItem >= 9.0
    }
}
