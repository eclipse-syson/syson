package P1 {
  part def Part1;
  part def Part3;

  part part1 : Part1 {
    event occurrence eventOccurence1;
  }

  part part2 : Part2 {
    part part3 : Part3 {
      event occurrence eventOccurence2;
    }
  }

  item def Item1;

  occurrence def Occurence1 {
    ref part :>> part1;
    ref part :>> part2;
    
    message message1 of Item1 
      from part1.eventOccurence1 to part2.part3.eventOccurence2;
  }
}