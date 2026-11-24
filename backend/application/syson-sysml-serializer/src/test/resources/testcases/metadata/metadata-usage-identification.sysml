// Covers metadata usage name and short name (MetadataRenderer#serializeMetadataUsage)
package MetadataUsageIdentification {
    metadata def M;
    part def P {
        @m : M;
        metadata <s> n : M;
    }
}
