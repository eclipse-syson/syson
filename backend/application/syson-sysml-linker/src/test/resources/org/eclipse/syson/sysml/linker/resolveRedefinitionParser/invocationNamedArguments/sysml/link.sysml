package P {
    attribute def Value;

    constraint def C {
        in left : Value;
        in right : Value;

        left == right
    }

    attribute leftValue : Value;
    attribute rightValue : Value;

    constraint test {
        C(
            right = rightValue,
            left = leftValue
        )
    }
}
