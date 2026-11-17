public package other {
  private part def part1;
}

public package current {
  private import $::other::part1;
  part part2 : part1;
}
