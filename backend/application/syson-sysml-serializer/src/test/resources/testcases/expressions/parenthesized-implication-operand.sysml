// detected with ode4hera_simulation-and-analysis_2.0.2
package ParenthesizedImplicationOperand {
    doc /* Covers an explicitly grouped logical expression after implies. */

    assert constraint {
        conditionA implies
            ((conditionB or conditionC) and conditionD and conditionE)
    }
}
