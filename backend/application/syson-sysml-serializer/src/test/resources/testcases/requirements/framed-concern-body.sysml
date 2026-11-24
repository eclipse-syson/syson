package FramedConcernBody {
    doc /* Covers a framed concern reference completed by a requirement body. */

    concern targetConcern;
    requirement enclosingRequirement {
        frame targetConcern {
        }
    }
}
