package LambdaComment {
    private import ScalarValues::Integer;
    private import ControlFunctions::*;
    attribute positives = (1, 2)->select {
        in x : Integer { comment about x /* Predicate input. */ }
        x > 0
    };
}
