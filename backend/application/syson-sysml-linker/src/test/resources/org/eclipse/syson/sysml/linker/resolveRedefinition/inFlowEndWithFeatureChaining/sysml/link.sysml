package P1 {
  item def Item1;
  
  part part1 {
    port port1 {
      out item1 : Item1;
    }
  }

  interface interface1 connect part1.port1 to part2.port1 {
    flow flow1 of Item1 
      from part1.port1.item1 to part2.port1.item1;
  }

  part part2 {
    port port1 {
      in item1 : Item1;
    }
  }
}