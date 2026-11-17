public package definitions {
  public part def part1;
}

public package other {
  private import $::definitions::*;
}

public package current {
  private import all $::other::*;
  part part2 : part1;
}
