// Extracted from ImportSysMLModelTest#checkNameResolutionProcessWithDuplicatedName
package p1 {
    part pa1 {
        part pa11 :> pa1;
        part pa1;
    }
}
