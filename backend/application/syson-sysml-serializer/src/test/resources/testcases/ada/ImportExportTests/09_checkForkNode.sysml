action action1 {
    action a1;
    action a2;
    fork fork1;
    then a1;
    then a2;
    first start then fork1;
}
action action2 {
    action a1;
    action a2;
    fork fork1 {
        /* doc1 */
    }
    then a1;
    then a2;
    first start then fork1;
}
