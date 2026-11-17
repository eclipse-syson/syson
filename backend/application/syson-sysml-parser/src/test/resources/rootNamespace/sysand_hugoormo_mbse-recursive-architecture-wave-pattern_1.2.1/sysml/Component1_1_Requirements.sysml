package Component1_1_Requirements {
    
    /*
    PATTERN RULES FOR COMPONENT1_1_REQUIREMENTS:
    
    1. LEAF REQUIREMENT SEMANTICS:
       - Terminal decomposition node (no further child needs)
       - Still decontextualize parent need at this level's context
       - Inherit constraint from parent via subsetting (no hardcoding)
       - Subject redefines parent (even at terminal level)
       - No derived requirement (leaf has no decomposition)
    
    2. CONFIGURATION BINDING STRATEGY:
       - Blackbox configuration (Config_bb): attribute binding to action output
       - Graybox configuration (Config_gb): if present, MUST remain abstract
       - Whitebox configuration (Config_wb): integration level for verification
       - Binding is set once at blackbox level; inherited, never overridden
    
    3. SUBJECT REDEFINITION:
       - Original requirement subject: redefines parent subsystem context
       - Narrowing chain continues from L0→L1→L2→L3
       - No further narrowing after this point

    4. RECURSIVE CLOSURE SEMANTICS:
       - This leaf requirement validates its parent child need by subsetting it.
       - It is then verified when the leaf component architecture satisfies it.
       - See MyFramework::MyFrameworkRecursiveClosureSemantics for the reusable rule.
    */
    
    comment patternRules /*
        This package implements the MBSE RecursiveArchitecture Weave Pattern at L3 (Component level).
        As a LEAF level, only original requirements exist (no decomposition).
        Violations detected by:
        - Syside syntax/semantic validation
        - Constraint evaluation on live behavioral outputs
        - Cross-level consistency verification
    */
    
    comment payloadQualityGainComponent1_1ModelerComment
        about payloadQualityGainComponent1_1
        locale "en_US"
    /* L3 leaf requirement: final transformation of the L2 derived need
     * (no further decomposition).
     */
    requirement payloadQualityGainComponent1_1
        subsets
            Subsystem1_Requirements::payloadQualityGainPayloadBuilderDerived
                .payloadQualityGainPayloadPrebuilderNeed {
        
        comment transformedRequirementClosureSemantics /*
            NEED VALIDATION: By subsetting the L2 payloadPrebuilder child need, this L3
            requirement validates the transformation of that need into the Component1_1
            context.

            REQUIREMENT VERIFICATION: This transformed leaf requirement is verified by
            Component1_1_Configuration::payloadPrebuilder_bb and the Component1_1 terminal
            verification cases.
        */
        
        comment component1_1AllocationRationale /*
            DESIGN DECISION: Component1_1 (PayloadPrebuilder) allocated 4.0 units (leaf terminal node).
            RATIONALE: Single atomic component; no further decomposition possible; allocation matches parent prebuilder need.
        */
        subject payloadPrebuilder redefines payloadBuilder subsets Component1_1_Configuration::payloadPrebuilder_bb;
        doc
        /* The payload shall achieve a gain of quality through Component1_1 
         * of at least 4 units. (Here we decontextualize the requirement from L2)
         * This requirement is derived from the subsetted Subsystem1 derived requirement.
         * With this we ensure that Component1_1 will perform as needed in the
         * context of Subsystem1 by inheriting the constraint from the 
         * Subsystem1 requirements and redefining the subject.
         * 
         * This is a leaf-level requirement with no further decomposition.
         */
        comment payloadQualityGainComponent1_1ConstraintModelerComment
            about payloadQualityGainComponent1_1
            locale "en_US"
        /* Constraint inherited from parent requirement through subsetting. */
    }
}
