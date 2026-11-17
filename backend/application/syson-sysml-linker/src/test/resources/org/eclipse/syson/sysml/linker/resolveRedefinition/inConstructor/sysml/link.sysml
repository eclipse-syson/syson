package P1 {
  part def Part1 {
    attribute attribute1;
  }

  verification verifivation1 {
    subject part1 : Part1;
    action action1 {
      in input1;
      out output1 = new part1(attribute1 = input1);
    }
  }
}