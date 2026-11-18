package P { 
    state def S { 
        state s1; 
        state s2; 
        transition first s1 then s2; 
        accept x then s2;
        state s3;
    } 
    part keep; 
}
package Q;
