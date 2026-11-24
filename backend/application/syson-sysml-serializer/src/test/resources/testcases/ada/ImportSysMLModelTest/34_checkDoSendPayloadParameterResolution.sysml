// Extracted from ImportSysMLModelTest#checkDoSendPayloadParameterResolution
package Package1 {
    attribute def Sig ;
    part p;

    state state1 {
        state s2;
        state s3;
        transition T1 first s2 accept s : Sig via p do send s to p then s3;
    }
}
