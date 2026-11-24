part part1 {
    private import SI::*;
    private import ScalarValues::*;
    action b {
        attribute f : Boolean;
    }
    action a1 {
        item def S1;
        item def S2;
        item def S3;
        port p1;
        port p2;
        port p3;
        action a1 accept s1 : S1 via p1;
        action a2 accept S2 via p2;
        accept S3 via p3;
        accept after 5 [min];
        accept when b.f;
    }
}
