package P1 {
  part def Part1;
  part def Part2;
  
  individual part def Part3 :> Part1 {
    part part2 : Part2;
  }
  
  individual part def Part4 :> Part2;
  
  individual part part3 : Part3 {
    snapshot part part5 {
      snapshot part2a : Part4 :>> part2;
    }
  }
}