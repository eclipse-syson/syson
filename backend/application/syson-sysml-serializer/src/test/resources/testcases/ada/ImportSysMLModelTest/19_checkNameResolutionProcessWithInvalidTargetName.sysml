// Extracted from ImportSysMLModelTest#checkNameResolutionProcessWithInvalidTargetName
package p1 {
    part pa1 {
        part pa11 :> pa1; // <-- 'pa1' resolve to the Package "pa1" in the current namespace. This model is not valid.
        package pa1;
    }
}
