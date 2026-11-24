// Covers a feature chain whose source is a grouped feature chain (FeaturePathExpressionRenderer)
package GroupedFeatureChainSource {
    part x {
        part ab {
            part c;
        }
        part 'a.b';
    }
    attribute plainName = (x.ab).c;
    attribute dottedQuotedName = (x.'a.b').c;
}
