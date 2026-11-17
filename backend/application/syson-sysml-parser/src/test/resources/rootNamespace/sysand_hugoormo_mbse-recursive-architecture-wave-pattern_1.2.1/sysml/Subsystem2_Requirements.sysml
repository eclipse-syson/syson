package Subsystem2_Requirements {
    
    /*
    PATTERN RULES FOR SUBSYSTEM2_REQUIREMENTS:
    
    1. TERMINAL SUBSYSTEM SEMANTICS:
       - No further decomposition (leaf at L2 in this pattern)
       - Original requirement only (no derived requirement for decomposition)
       - Decontextualize parent need at this level's blackbox context
       - Inherit constraint from parent via subsetting (no hardcoding)
       - Subject redefines parent (narrows context)
    
    2. CONFIGURATION BINDING STRATEGY:
       - Blackbox configuration (Config_bb): attribute binding to action output
       - Graybox configuration (Config_gb): if present, MUST remain abstract
       - Whitebox configuration (Config_wb): integration level for verification
       - Binding is set once at blackbox level; inherited, never overridden
    
    3. SUBJECT REDEFINITION:
       - Original requirement subject: redefines parent system context
       - Narrowing chain continues from L0→L1→L2
       - No further children, but may have behavioral realization

    4. RECURSIVE CLOSURE SEMANTICS:
       - This terminal requirement validates its parent child need by subsetting it.
       - It is then verified when the terminal subsystem architecture satisfies it.
       - See MyFramework::MyFrameworkRecursiveClosureSemantics for the reusable rule.
    */
    
    comment patternRules /*
        This package implements the MBSE RecursiveArchitecture Weave Pattern at L2 (Subsystem level).
        SUBSYSTEM2 is a TERMINAL branch: no further decomposition.
        Violations detected by:
        - Syside syntax/semantic validation
        - Constraint evaluation on live behavioral outputs
        - Cross-level consistency verification
    */
    
    comment payloadQualityGainPayloadVerifierModelerComment
        about payloadQualityGainPayloadVerifier
        locale "en_US"
    /* L2 terminal requirement branch: transforms L1 need and stops
     * recursion on this path.
     */
    requirement payloadQualityGainPayloadVerifier subsets
            SystemOfInterest_Requirements::payloadQualityGainSystemOfInterestDerived
                .payloadQualityGainPayloadVerifierNeed {
        comment transformedRequirementClosureSemantics /*
            NEED VALIDATION: By subsetting the L1 child need, this L2 terminal
            requirement validates the transformation of the payloadVerifier need into the
            Subsystem2 context.

            REQUIREMENT VERIFICATION: This transformed terminal requirement is verified by
            Subsystem2_Configuration::payloadVerifier_bb and the Subsystem2 verification cases.
        */
        subject payloadVerifier redefines mySystemOfInterest subsets Subsystem2_Configuration::payloadVerifier_bb;
        doc
        /* The payload shall achieve a gain of quality through Subsystem2 
         * of at least 7 units. (Here we decontextualize the requirement from L1)
         * This requirement is derived from the subsetted system of interest derived requirement.
         * With this we ensure that Subsystem2 will perform as needed in the
         * context of the system of interest by inheriting the constraint from the 
         * system of interest requirements and redefining the subject.
         * 
         * Subsystem2 is a terminal subsystem (not further decomposed), so this is
         * the final requirement at this level with no further gb decomposition.
         */
        comment payloadQualityGainPayloadVerifierConstraintModelerComment
            about payloadQualityGainPayloadVerifier
            locale "en_US"
        /* Constraint inherited from parent requirement through subsetting. */
    }
}
