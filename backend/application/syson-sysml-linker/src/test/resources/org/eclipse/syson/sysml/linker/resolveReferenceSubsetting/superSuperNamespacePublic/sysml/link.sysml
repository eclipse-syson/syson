public package current {
  part def part1 {
    public action action1;
  }

  part def part2 :> part1 {
    action action2;
  }

  part def part3 :> part2 {
    perform action callAction1 ::> action1;
  }
}
