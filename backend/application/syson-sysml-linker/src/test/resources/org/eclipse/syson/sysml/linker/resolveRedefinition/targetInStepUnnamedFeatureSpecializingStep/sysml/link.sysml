package 'Action Shorthand Example' {
  action action0 {
    in item1;
    out item2;
  }
  action def Action2 {
    in item2;
    out item3;
  }

  action def Action3 {
    action action1 :>> action0 {
      in item item1;
      out item;
    }

    flow from action1.item2 to action2.item2;

    then action action2: Action2 {
      in item item2;
      out item item3;
    }
  }
}