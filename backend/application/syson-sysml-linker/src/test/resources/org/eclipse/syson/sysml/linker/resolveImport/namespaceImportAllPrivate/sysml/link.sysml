public package other {
  private part def part1;
}

public package current {
  private import all $::other::*;
  part part2 : part1;
}
