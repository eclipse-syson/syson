package ExpressionOperatorVariants {
    attribute exclusive = a xor b;
    attribute bitwise = (a | b) & c;
    attribute identity = a === b;
    attribute inverse = a !== b;
    attribute magnitude = -(a ** 2);
    attribute powerRight = a ** b ** c;
    attribute powerLeft = (a ** b) ** c;
    attribute interval = 1..10;
    attribute typed = value as Type;
    attribute classified = value hastype Type;
}
