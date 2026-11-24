package MReqs {
    requirement <Mre1>;
}
package SReqs {
    requirement <Sre1>;
}
package Derivations {
    private import RequirementDerivation::*;
    private import MReqs::*;
    private import SReqs::*;
    #derivation connection {
        end #original ::> Mre1;
        end #derive ::> Sre1;
    }
}
