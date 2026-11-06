// Copyright (c) 2026 Obeo.
// SPDX-License-Identifier: EPL-2.0

package Example {
    private import ScalarValues::*;
    part def PD1 {
        attribute attr1_1 default = 2;
        attribute attr1_2 = attr1_1 * 2;
        part partPD2 : PD2;
        part partPD3 : PD3;
    }
    part def PD2 {
        attribute attr2_1 : String default = "value21";
    }
    part def PD3 {
        attribute attr3_1 : String default = "value31";
        variation part variantPart : AbstractVariant {
            variant part var1 : Var1;
            variant part var2 : Var2;
        }
    }
    enum def EnumerationDefinition {
        enum enum1;
        enum enum2;
    }
    part def AbstractVariant {
        attribute commonAttr : EnumerationDefinition default = EnumerationDefinition::enum1;
    }
    part def Var1 :> AbstractVariant {
        attribute attrVar1;
    }
    part def Var2 :> AbstractVariant {
        attribute attr1Var2;
        attribute attr2Var2;
    }

    part part1 : PD1 {
        :>> partPD3 {
            :>> variantPart :> PD3::variantPart::var1 {
                :>> attrVar1 = 10;
            }
        }
    }
    part part2 : PD1 {
        :>> attr1_1 = 8;
        :>> partPD2 {
            :>> attr2_1 = "value21Part2";
        }
        :>> partPD3 {
            :>> attr3_1 = "value31Part2";
            :>> variantPart :> PD3::variantPart::var2 {
                :>> commonAttr = EnumerationDefinition::enum2;
                :>> attr1Var2 = 20;
                :>> attr2Var2 = 30;
            }
        }
    }
} // package Example
