package MetadataBodyAliasImport {
    package Source {
        part widget;
    }
    metadata def Tag;
    metadata Tag {
        alias taggedWidget for Source::widget;
    }
    metadata Tag {
        private import Source::widget;
    }
}
