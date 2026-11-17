package P1 {
  part part1 {
    part part2 {
      part part3;
    }
  }
  
  part part4 {
    part part5 {
      part part6 {
        ref part part3;
      }
    }    
  }
      
  action {
    action action1
      assign part4.part5.part6.part3 := part1.part2.part3;
    
    then action action2
      assign part4.part5.part6.part3 := null;
  }
}