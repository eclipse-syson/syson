// The XMI counterpart stores Root directly as a resource root, without an owning membership.
// Its superclass is prelinked so this case isolates the redefinition's context lookup.
part def Root :> Base {
    attribute replacement redefines original;
}
