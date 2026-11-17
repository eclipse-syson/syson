package Subsystem2_Taxonomy {
    doc
    /* items, parts, ports, interfaces, and connections definitions in the 
     * context of the subsystem2.
     * This local taxonomy represents the payload-verifier owner's vocabulary and
     * maps it back to the shared system ontology.
     * Local naming differences are intentional and model organizational ownership of
     * product taxonomies. See
     * MyFramework::MyFrameworkFoundationSemantics::localTaxonomyOwnershipRule.
     */

    abstract item mySubsystem2ExchangeItems subsets MySystemOntology::systemExchangeItems [0..*];

    abstract part mySubsystems2 subsets MySystemOntology::systems [0..*] {
        attribute qualityContribution defined by ScalarValues::Real;
    }
}
