package FlowSpecializations {
    doc /* Covers flow specializations and their multiplicity before endpoints. */

    item exchanges;
    interface def Exchange {
        end port supplier;
        end port consumer;
        flow transferred subsets exchanges [0..*] from supplier to consumer;
    }
}
