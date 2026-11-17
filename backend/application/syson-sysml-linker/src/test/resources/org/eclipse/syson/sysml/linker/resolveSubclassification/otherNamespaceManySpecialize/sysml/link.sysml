public package other {
  public part def part1 {
    attribute attribute1;
  }
  public part def part2 {
    attribute attribute2;
  }
}

public package current {
  part def part2 :> $::other::part1, $::other::part2 {
    attribute attribute3;
  }
}
