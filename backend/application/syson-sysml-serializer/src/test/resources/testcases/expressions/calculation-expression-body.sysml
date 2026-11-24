package CalculationExpressionBody {
    calc def Score {
        in left;
        in right;
        return result = left * (right + 1);
    }

    calc score : Score {
        in left = 2;
        in right = 3;
        return result;
    }
    constraint def Positive { score > 0 }
    assert constraint { (score > 0) and (score < 20) }
}
