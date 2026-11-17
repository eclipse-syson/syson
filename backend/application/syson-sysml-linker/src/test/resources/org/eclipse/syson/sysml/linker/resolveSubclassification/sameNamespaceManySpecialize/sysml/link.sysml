public package current {
  public part def part1 {
    attribute attribute1;
  }
  public part def part2 {
    attribute attribute2;
  }

  part def part2 :> part1, part2 {
    attribute attribute3;
  }
}
