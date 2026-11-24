package p1 {
    package 'p 2' {
        action def 'A 1';
    }
    action 'a 2' : 'p 2'::'A 1';
}
