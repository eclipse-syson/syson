package {
    part def Part1 {
        attribute attribute1;
    }
    part def Part2 :> Part1 {
        attribute attribute2;
    }
}
