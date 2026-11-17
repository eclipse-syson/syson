public package other {
  public part def part1 {
    public part11 {
      public part111;
    }
  }
}

public package current {
  alias importAlias for $::other::part1; 
  private import importAlias;
  part part2 : importAlias::part111;
}
