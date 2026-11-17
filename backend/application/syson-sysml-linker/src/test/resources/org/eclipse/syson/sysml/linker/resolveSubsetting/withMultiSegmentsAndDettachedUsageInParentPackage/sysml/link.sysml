package P1 {
  action def Action1;
  
  action action1: Action1;

  part def Part1 {
    perform action action1: Action1;
  }

  part part1: Part1 {
    perform action1 :>> Part1::action1;
  }
}