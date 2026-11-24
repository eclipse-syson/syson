package CommentAnnotationLocations {
    item def Component;
    comment about Component /* Linked to the definition. */
    doc locale "en" /* Package documentation. */
    rep language "text" /* Opaque representation. */

    part component : Component; // Comment on the same line as a usage.
    part enclosing {
        // Comment before the first child.
        part nested;
        // Comment at the end of a usage body.
    }
}
