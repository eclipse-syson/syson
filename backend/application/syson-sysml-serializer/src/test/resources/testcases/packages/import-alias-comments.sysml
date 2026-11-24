package ImportAliasComments {
    package Source {
        item sourceItem;
    }
    private import Source::*;
    part def Container {
        port porig1;
        alias po1 for porig1;
    }
    // Preserve the final comment after an import.
}
