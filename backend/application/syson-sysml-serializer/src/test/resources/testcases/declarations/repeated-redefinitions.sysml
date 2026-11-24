// detected with ode4hera_simulation-and-analysis_2.0.2
package RepeatedRedefinitions {
    doc /* Covers separate repeated redefinition clauses. */

    part baseOne;
    part baseTwo;
    ref part redefines baseOne redefines baseTwo;
}
