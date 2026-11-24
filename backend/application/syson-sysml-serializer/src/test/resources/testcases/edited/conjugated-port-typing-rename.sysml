// EditedReferenceTargetSerializationTest links the typing of p to the conjugated definition that PD owns, then renames
// PD: the typing must name the renamed definition, with or without parser spellings (ReferenceTargets)
package U {
    port def PD;
    ref port p : ~PD;
}
