package MetadataBodyComment {
    doc /* Covers comments interleaved with fields in a metadata body. */

    metadata VerificationMethod {
        comment guidance about VerificationMethod locale "en_US"
        /* Keep the guidance before the field. */
        kind = VerificationMethodKind::analyze;
    }
}
