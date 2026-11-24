// detected with ode4hera_simulation-and-analysis_2.0.2
package MetadataFieldTrailingComment {
    doc /* Covers a same-line comment following a metadata body field. */

    attribute def Value;

    metadata def Marker {
        attribute fieldOne : Value;
        attribute fieldTwo : Value;
        attribute fieldExpression : Value;
    }

    part def Container {
        @Marker {
            :>> fieldOne = 1; // Keep this field explanation.
            :>> fieldTwo = 2;
            :>> fieldExpression = (1 + 2) * 3;
        }
    }
}
