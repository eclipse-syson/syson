// Covers usage prefixes of successions (SuccessionRenderer)
package SuccessionUsagePrefixes {
    metadata def M;
    action def A {
        action a;
        action b;
        abstract succession s1 first a then b;
        ref succession s2 first a then b;
        in succession s3 first a then b;
        derived constant succession s4 first a then b;
        variation succession s5 first a then b;
        out abstract ref succession first a then b;
        #M succession s6 first a then b;
        abstract first a then b;
        succession s7 first a then b;
    }
    part def P {
        part a;
        part b;
        ref succession s8 first a then b;
        abstract first a then b;
        ref first a then b;
    }
}
