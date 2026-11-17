package Quantities {
    public attribute radius;
}

package Shapes {
    private import Quantities::*;

    part def General;

    part def Specific :> General {
        // The private object on the resolution path is the import, not the public target membership.
        attribute redefinedRadius :>> radius;
    }
}
