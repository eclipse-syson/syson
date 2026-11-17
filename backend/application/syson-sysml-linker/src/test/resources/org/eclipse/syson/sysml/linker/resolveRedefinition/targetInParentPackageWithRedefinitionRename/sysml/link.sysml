package P1 {
  part part1;

  part def Part2 {
    part part1redefined :>> part1;
  }

  part part2 : Part2 {
    part :>> part1redefined;
  }
}