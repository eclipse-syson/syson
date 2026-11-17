package P1 {
  part def Part1 {
    attribute attribute1;
  }

  state state1 {
    in part1 : Part1;
    state maintenance {
      entry assign part1.attribute1 := 1;
    }
  }
}