package FeatureChainTyping {
    part enclosing {
        attribute measured : sensor.reading [2];
        ref observed : sensor.target;
        part typedAndSubsetted : sensor.housing :> parts;
    }
    action enclosingAction {
        accept [1] Signal;
        accept [2] sensor.reading via inlet;
        accept sensor.reading [4];
        accept received : sensor.reading;
    }
}
