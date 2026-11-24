// Covers user keywords before the end feature keyword (DefinitionAndUsageRenderer#writeEndModifiers)
package EndUserKeywordOrder {
    metadata def K;
    part def A;
    connection def C {
        end #K part a : A;
    }
}
