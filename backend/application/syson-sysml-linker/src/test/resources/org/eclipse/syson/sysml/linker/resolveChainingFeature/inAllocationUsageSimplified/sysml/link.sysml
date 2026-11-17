package P {
  part def Part1;
  part def Part1a :> Part1 {
    perform action action1;
  } 

  action action2 {
    action action1;
  }

  part part1a : Part1a {
    perform action2.action1 :>> action1;
  }

  part def Part2;
  part def Part2a :> Part2;

  part part2a : Part2a {
    part part3 {
      // should be
      // perform action2.action1;
      perform action action1;
    }
  }

  allocation def Allocation1 {
    end end1 : Part1;
    end end2 : Part2;
  }

  allocation allocation1 : Allocation1 allocate end1 ::> part1a to end2 ::> part2a {
    allocate part1a.action1 to part2a.part3.action1;
  }
}