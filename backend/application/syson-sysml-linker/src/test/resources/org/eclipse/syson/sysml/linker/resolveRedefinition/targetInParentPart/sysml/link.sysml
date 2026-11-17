package P1 {
  part parentPart {
    part part1;

    part part2 {
      part :>> part1;
    }
  }
}