package CaseObjectivesAndComments {
    part def System;
    requirement def Limit {
        require constraint { size < 10 }
    }
    requirement limit : Limit;

    analysis def Check {
        subject system : System;
        objective limit;
        // Preserve the end of the analysis body.
    }
    verification def Verify {
        subject system : System;
        objective {
            verify limit;
        }
        // Preserve the end of the verification body.
    }
    use case def Operate {
        subject system : System;
        actor operator;
    }
}
