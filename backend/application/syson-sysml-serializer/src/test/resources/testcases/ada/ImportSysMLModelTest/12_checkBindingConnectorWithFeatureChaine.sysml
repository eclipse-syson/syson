// Extracted from ImportSysMLModelTest#checkBindingConnectorWithFeatureChaine
package pk1 {
    action a1 {
        in item i1;

        action a11 {
            in item i11;
        }
        action a21 {
            in item i21;
        }
        bind i1 = a21.i21;
        binding b2 bind a11.i11 = a21.i21;
    }
}
