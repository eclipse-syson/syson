// Covers derived before abstract/variation prefix order (DeclarationRenderer#serializePrefixModifiers)
package DerivedAbstractPrefixOrder {
    part def P {
        derived abstract attribute a;
        in derived variation constant ref attribute x;
    }
}
