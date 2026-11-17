public package other {
  part def part1;
  alias part1alias for part1;
}

public package grandfather {
  private package father {
    part def part2;
    alias part2alias for part2;
    public package current {
      part def part3;
      alias part3alias for part3;
      public package child {
        part def part4;
        alias part4alias for part4;
      }
      part part5 : child::part4;
    }
  }
}
