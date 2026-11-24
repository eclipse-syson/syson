package Parts {
    part <p1> part1 {
        port po1;
    }
    part <p2> part2 {
        port po2;
    }
    part <p3> part3 {
        port po3;
    }
}
package Connections {
    private import Parts::part1;
    private import Parts::part2;
    connect p1 to p2;
    connect [1] p1 to [1] p2;
    connect (p1, p2, Parts::part3);
    connect p1.po1 to p2.po2;
}
