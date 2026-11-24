package ExpressionPrecedenceContexts {
    attribute source = (a + b) * c;
    attribute alternative = a ?? (b + c);
    attribute logical = (a or b) and not c;
    attribute comparison = (a + b) >= c and d != e;
    attribute call = f((a, b), c[0]);

    constraint { ((a + b) * c > d) implies (e or f) }
    filter (a + b) > c;
}
