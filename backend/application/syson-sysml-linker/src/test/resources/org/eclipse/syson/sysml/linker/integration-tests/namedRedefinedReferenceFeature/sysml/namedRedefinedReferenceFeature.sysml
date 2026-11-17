// "GIVEN a redefinition of a part with the same name as the redefined part, WHEN importing the model, THEN the redefintion's redefined feature is correctly resolved."
package Package1 {
    part def A;
    part def B {
        part x : A [0..*];
    }

   part y : B {
        x :>> x; // The second x should point B::x, not to y::x
   }
}
