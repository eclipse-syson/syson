package IndividualOccurrencePortions {
    occurrence def Event;
    individual occurrence def Session {
        snapshot opening;
        timeslice active;
    }

    individual occurrence session : Session {
        individual snapshot checkpoint : Session;
        timeslice ending;
    }

    occurrence events : Event [0..*];
}
