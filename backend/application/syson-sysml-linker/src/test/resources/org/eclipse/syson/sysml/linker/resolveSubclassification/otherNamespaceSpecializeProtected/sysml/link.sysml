public package other {
  protected part def part1 {
    attribute attribute1;
  }
}

public package current {
  part def part2 :> $::other::part1 {
    attribute attribute2;
  }
}
