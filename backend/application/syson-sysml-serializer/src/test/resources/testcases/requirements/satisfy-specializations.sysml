package SatisfySpecializations {
    doc /* Covers the complete usage declaration of named satisfy requirements. */

    requirement firstRequirement;
    requirement secondRequirement;
    part container {
        satisfy requirement firstSatisfaction subsets firstRequirement;
        satisfy requirement secondSatisfaction redefines firstSatisfaction subsets secondRequirement;
    }
}
