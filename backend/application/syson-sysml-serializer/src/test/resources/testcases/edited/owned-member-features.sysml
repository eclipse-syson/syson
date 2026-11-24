// SafeEmfAccessTest reads the memberships of P and A through Relationship::ownedRelatedElement, a stored containment
// that derived references subset, and through the derived owned member feature and parameter, which reach it through
// intermediate redefinitions; the comment is not an owned member feature (SafeEmfAccess#unresolvedValues)
part def P {
    ref r;
    comment /* not a feature */
}
action def A {
    in x;
}
