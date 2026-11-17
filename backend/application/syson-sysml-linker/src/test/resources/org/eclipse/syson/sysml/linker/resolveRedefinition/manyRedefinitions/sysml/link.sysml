package P1 {
  part def Part1;
  part def Part2;

  part part1 : Part1;

  part part1a : Part1;

  occurrence def Occurence1 {
    ref part :>> part1;
  }

  occurrence occurence1a : Occurence1 {
    part :>> part1 :>> part1a;
  }
}