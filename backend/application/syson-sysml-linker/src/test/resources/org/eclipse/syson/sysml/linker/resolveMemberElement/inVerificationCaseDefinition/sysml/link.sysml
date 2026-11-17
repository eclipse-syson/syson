package P1 {
  part def Part1 {
    attribute attribute1;
  }

  requirement requirement1 {
    subject part1 : Part1;
    in input1;

    require constraint { 
        input1 == part1.attribute1 and 
        input1 <= 1
    }
  }

  verification def Verifivation1 {
    subject part1a : Part1;
    objective objective1 {
      verify requirement1;
    }

    action action1 {
      in input1a;
      out output1 = requirement1(part1 = part1a, input1 = input1a);
    }
  }
}