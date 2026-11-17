public package other {
  public part def part1 {
    part def part11;
  }
}

public package current {
  private import $::other::part1;
  part part2 : part1::part11;
}
