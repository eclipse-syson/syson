action action1 {
    action a1;
    action a2;
    fork fork1;
    then a1;
    then a2;
    merge merge1;
    first start then fork1;
    first a1 then merge1;
    first a2 then merge1;
}
