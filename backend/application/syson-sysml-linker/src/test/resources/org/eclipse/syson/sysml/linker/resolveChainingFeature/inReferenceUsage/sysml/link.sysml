package P {
  
  part def Part1;
  part def Part2;
  part def Part3;
  
  part part1: Part1 {        
    part part2: Part2 {
      part part3: Part3;
    }
  }

  part trailer1: Part2 {
    ref part3ref : Part3 = part1.part2.part3 {
    }
  }
}
