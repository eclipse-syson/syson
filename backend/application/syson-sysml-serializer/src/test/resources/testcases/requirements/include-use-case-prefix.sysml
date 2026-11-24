// Covers include use case prefixes and typing (IncludeUseCaseRenderer)
package IncludeUseCasePrefix {
    metadata def K;
    use case def U;
    use case def V {
        use case u1;
        use case u2;
        use case u3;
        use case u4;
        use case u5;
        include u1 : U;
        in include u2;
        individual include u3;
        #K include u4;
        derived include u5;
    }
}
