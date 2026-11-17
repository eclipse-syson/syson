public package current {
  part def part1 {
    public action action1;
  }

  part def part2 :> part1 {
    perform action callAction1 ::> action1;
  }
}
