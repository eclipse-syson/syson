package P1 {
  action action1;

  part part1 {
    variation perform action action2 {
      variant perform action1;
    }
  }

  part part2 {
    part :>> part1 {
      perform action :>> action2 = action1;
    }
  }
}