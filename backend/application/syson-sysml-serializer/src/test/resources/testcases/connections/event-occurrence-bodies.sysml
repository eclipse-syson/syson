package EventOccurrenceBodies {
    part enclosing {
        event start {
            attribute delay;
        }
        event occurrences.stop {
            redefines delay = source.delay.value;
        }
        event stop {
            redefines delay = source.delay.value;
        }
        event occurrence named {
            attribute delay;
        }
        event occurrence redefining :>> stop {
            attribute delay;
        }
        event done = finished {
            attribute delay;
        }
    }
}
