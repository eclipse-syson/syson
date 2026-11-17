package InteractingSystem_Taxonomy {
    doc
    /* items, parts, ports, interfaces, and connections definitions in the 
     * context of the interacting system.
     * At this level it may be relevant to inherit aspects of both the operational and system ontologies, as the interacting system 
     * is a system that participates in the operational context and interacts with the system of interest.
     * This is a local product taxonomy for the external interacting-system owner. It may
     * intentionally differ from the system-of-interest taxonomy while preserving
     * traceability through subsetting and specialization.
     * See MyFramework::MyFrameworkFoundationSemantics::localTaxonomyOwnershipRule.
     */

    abstract item myInteractingSystemExchangeItems subsets MySystemOntology::systemExchangeItems [0..*];

    abstract part myInteractingSystems subsets MySystemOntology::systems [0..*] {
        attribute qualityContribution defined by ScalarValues::Real;
    }

}
