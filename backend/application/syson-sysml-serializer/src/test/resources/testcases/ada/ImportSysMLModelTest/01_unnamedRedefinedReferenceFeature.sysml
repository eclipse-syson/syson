// Extracted from ImportSysMLModelTest#unnamedRedefinedReferenceFeature
package p1 {
    part def Logical {
        part component{
            part component2;
        }
    }
    part l : Logical {
        part :>> component;
    }

    part c2  = l.component.component2;
}
