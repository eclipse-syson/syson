// Extracted from ImportSysMLModelTest#testReferentialUsages
package root {
    // Referential because contained in package
    part part1 {
        part part11; // Composite because no "ref" keyword
        attribute attr; // ReReferential because AttributeUsage
        in part part22; // Referential because "directed" feature
        ref part part13; // Referential since using "ref" keyword
    }
    // Referential because contained in package
    action a1 {
        action a11; // Composite because no "ref" keyword
        action a12; // Composite because no "ref" keyword
        succession suc1 first a11 then a12; // Referential because is a Connector
    }

    use case def ucd_1 {
        objective c; // Composite because no "ref" keyword
        subject subject_1; // Ref because ReferenceUsage
        actor partU_1; // Ref because in actor
    }
}
