// Covers the spelling and count of metadataItems subsettings after a redefinition (SpecializationClausePlanner)
package MetadataItemsSubsettingSpellings {
    metadata def M {
        item a;
        item b;
        derived ref item x redefines a subsets Metadata::metadataItems;
        derived ref item y :>> a :> Metadata::metadataItems;
        derived ref item z subsets b subsets Metadata::metadataItems;
        derived ref item w :> b, Metadata::metadataItems;
        derived ref item d redefines a subsets Metadata::metadataItems subsets Metadata::metadataItems;
    }
}
