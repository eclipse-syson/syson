package P {
  action action1 {
    action action2;
  }
  
  part part1 {
    perform action1.action2;
  }
}