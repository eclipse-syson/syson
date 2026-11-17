package P1 {
  verification verification1 {
    subject part1;
    action action1 {
      in part part1;
      out output1;
    }
  }
  
  part part2 {
    perform verification1.action1 {
      in part :>> part1;
      out ouput1;
    }
  }
}