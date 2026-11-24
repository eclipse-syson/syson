action action1 {
    action a1;
    action a2;
    fork fork1;
    then a1;
    then a2;
    join join1;
    first start then fork1;
    first a1 then join1;
    first a2 then join1;
    first join1 then done;
}
