// detected with ode4hera_simulation-and-analysis_2.0.2
package BodyMetadataUsage {
    doc /* Covers a named metadata usage in a definition body. */

    metadata def Marker;

    part def Container {
        metadata marker : Marker;
        part nested;
    }
}
