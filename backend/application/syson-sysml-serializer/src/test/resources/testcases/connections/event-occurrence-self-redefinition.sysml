// Covers anonymous event occurrences redefining an inherited occurrence (EventOccurrenceRenderer)
package EventOccurrenceSelfRedefinition {
    occurrence def O {
        occurrence q;
    }
    occurrence def O2 :> O {
        event occurrence :>> q;
    }
}
