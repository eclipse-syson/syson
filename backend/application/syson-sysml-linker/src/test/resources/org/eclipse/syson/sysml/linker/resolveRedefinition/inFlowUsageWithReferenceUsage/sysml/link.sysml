action action1 {
  in in1;
  out out1;
}

flow action1.out1 
  to action2.in1;

action action2 {
  in in1; 
  out out1;
}