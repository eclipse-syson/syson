public package current {
  part def part1 {
    public attribute attribute1;
  }

  part def part2 :> part1 {
    attribute attribute2 :> attribute1;
  }
}
