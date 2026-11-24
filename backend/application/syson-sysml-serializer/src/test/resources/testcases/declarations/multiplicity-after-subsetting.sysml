// detected with ode4hera_simulation-and-analysis_2.0.2
package MultiplicityAfterSubsetting {
    doc /* Covers a multiplicity placed after a subsetting clause. */

    attribute def Value;

    attribute def Base {
        attribute values;
    }

    attribute def Derived :> Base {
        :>> values :> Value[1..*];
    }
}
