package Component1_1_Taxonomy {
    doc
    /* items, parts, ports, interfaces, and connections definitions in the 
     * context of the component1_1.
     * This is a component-owner local taxonomy. Repetition across recursive levels is
     * intentional; subsetting maps local product vocabulary back to shared ontology
     * concepts. See MyFramework::MyFrameworkFoundationSemantics::localTaxonomyOwnershipRule.
     */

    abstract item myComponent1_1ExchangeItems subsets MySystemOntology::systemExchangeItems [0..*] nonunique;

    abstract part myComponents1_1 subsets MySystemOntology::systems [0..*] {
        attribute qualityContribution defined by ScalarValues::Real;
    }

}
