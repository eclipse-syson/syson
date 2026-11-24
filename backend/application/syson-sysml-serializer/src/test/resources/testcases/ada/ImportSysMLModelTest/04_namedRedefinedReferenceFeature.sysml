// Extracted from ImportSysMLModelTest#namedRedefinedReferenceFeature
package Package1 {
    part def A;
    part def B {
        part x : A [0..*];
    }

   part y : B {
        x :>> x; // The second x should point B::x, not to y::x
   }
}
