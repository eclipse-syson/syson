attribute a;

private part def General {
    public attribute target;
}

part def Specific :> General {
    // General is a private qualifier, while target is a public, otherwise inherited feature.
    attribute redefinedTarget :>> General::target :> a;
}
