// Covers the parenthesized n-ary connector form with only two ends (ConnectorRenderer)
package NaryConnectorTwoEnds {
    part def Device {
        part a;
        part b;
        connect (a, b);
        connection c connect (a, b);
    }
}
