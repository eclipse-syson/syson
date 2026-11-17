public package other {
  public part def part1;
}

public package current {
  private import $::other::part1;
  part part2 : part1;
}
