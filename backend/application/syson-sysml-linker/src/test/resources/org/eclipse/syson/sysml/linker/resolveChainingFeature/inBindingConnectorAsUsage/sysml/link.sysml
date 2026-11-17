package P {

  part part1 {				
    part reference1;
  }

  bind part1.reference1 = part2.part3;

  part part2 {				
    part part3;
  }

}
