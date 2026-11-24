// Covers an unnamed valued event whose path is a quoted name containing a space (EventOccurrenceRenderer)
package EventQuotedPathWithSpace {
    occurrence def O {
        occurrence 'q r';
        occurrence s;
    }
    occurrence def O2 :> O {
        event 'q r' = s;
    }
}
