package P1 {
  part def Part1;

  verification action1;

  part part1 : Part1 {
    perform action1;
  }

  individual def Individual1 :> Part1;

  individual individual1 : Individual1 :> part1 {
    timeslice timeslice1 {
      perform action :>> action1;
    }
  }
}