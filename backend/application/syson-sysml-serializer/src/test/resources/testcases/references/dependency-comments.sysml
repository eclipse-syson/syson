package DependencyComments {
    package Sources {
        package Component;
        package Consumer;
    }
    private import Sources::*;
    dependency from Component to Consumer;
    // Preserve the final comment after a dependency.
}
