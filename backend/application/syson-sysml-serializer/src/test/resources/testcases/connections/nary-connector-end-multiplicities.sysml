// Covers n-ary connector end multiplicities and reference keywords (ConnectorEndRenderer)
package NaryConnectorEndMultiplicities {
    part a;
    part b;
    part c;
    connect ([1] a, [2] b, c);
    connect (n1 ::> a, n2 references b, c);
}
