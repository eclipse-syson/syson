// Covers body metadata kept after declaration-part relationships (ElementSyntaxQueries#isLeadingUserKeywordMetadata)
package BodyMetadataNotPromoted {
    metadata def M;
    part def Q;
    part def P :> Q { @M; }
    part y;
    part z :> y { @M; }
    part w { doc /* x */ @M; }
}
