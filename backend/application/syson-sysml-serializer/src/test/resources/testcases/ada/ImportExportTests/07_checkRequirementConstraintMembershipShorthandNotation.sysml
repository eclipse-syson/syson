requirement r1;
requirement r3 {
    requirement r4 {
        requirement r5;
    }
    require r1;
    require r4.r5;
    require r4 {
        doc /* Some doc */
    }
}
