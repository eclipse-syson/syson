package P1 {
  item def Item1;

  port def Port1 {
    out item item1 : Item1;
  }

  part def Part1 {
    port port1out : Port1;
  }

  part def Part2 {
    port port1in : ~Port1;
    port port1out : Port1;
  }

  part part1 : Part1;
  part part2 : Part2;

  flow of Item1
    from part1.port1out.item1 to part2.port1in.item1;
}