// GIVEN unnamed redefinition used in a FeatureChain, WHEN importing the model, THEN chain should reference the redefined feature instead of the inherited one.
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
