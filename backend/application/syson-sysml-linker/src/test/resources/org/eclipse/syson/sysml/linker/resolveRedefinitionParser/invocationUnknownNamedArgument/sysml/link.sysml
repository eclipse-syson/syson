package P {
    attribute def Value;

    constraint def C {
        in left : Value;

        left == left
    }

    attribute leftValue : Value;

    constraint test {
        C(missing = leftValue)
    }
}
