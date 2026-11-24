package MetadataPackageAlternatives {
    package Source {
        part widget;
    }

    metadata def Tag {
        ref :>> annotatedElement;
        ref redefines annotatedElement;
        alias taggedItem for Source::widget;
        private import Source::widget;
    }

    metadata Tag {
        ref :>> annotatedElement;
        ref redefines annotatedElement;
        alias taggedItem for Source::widget;
        private import Source::widget;
    }

    private import Source::*[true][true];
}

filter true;
alias sourceAlias for MetadataPackageAlternatives::Source;
