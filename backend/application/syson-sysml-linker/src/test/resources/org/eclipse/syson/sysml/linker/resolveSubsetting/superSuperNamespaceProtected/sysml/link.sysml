public package current {
  part def part1 {
    protected attribute attribute1;
  }

  part def part2 :> part1 {
    attribute attribute2;
  }

  part def part3 :> part2 {
    attribute attribute3 :> attribute1;
  }
}
