// Reproduces a subsetting resolved from the subsetting feature's type instead of its owning type.
package LocalDefinitions {
    part def BasePart {
        attribute selectedFeature;
    }

    part def RefinedPart :> BasePart {
        attribute :>> selectedFeature;
    }
}

package Test {
    part def TestedPart :> LocalDefinitions::RefinedPart {
        // Must resolve to RefinedPart::selectedFeature; the previous lookup resolved BasePart::selectedFeature.
        attribute :> selectedFeature : LocalDefinitions::BasePart;
    }
}
