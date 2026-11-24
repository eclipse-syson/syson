// identified while checking syside-views
package ImplicitSelfCastFilter {
    doc /* Covers an implicit-self cast followed by a classification test in a view filter. */

    view def FilteredView {
        filter as SysML::Definition istype SysML::Definition;
    }
}
