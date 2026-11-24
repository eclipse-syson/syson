package VariationVariants {
    part def Basic;
    variation part def Choice :> Basic {
        variant part selected : Basic;
        variant part standby : Basic;
    }

    variation action def Paths {
        variant action primary;
        variant action fallback;
    }
}
