package P1 {
  part part1 {
    part part2;
  }

  individual individual1 :> part1 {
    timeslice individual2 {
      ref individual :>> part2;
    }
  }
}