public package other {
  protected part def part1 {
    public part11 {
      public part111;
    }
  }
}

public package current {
  private import $::other::part1::**;
  part part2 : part111;
}
