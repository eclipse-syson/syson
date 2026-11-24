package Requirements {
    requirement <Req1> {
        doc /* Some doc */
    }
    requirement def REQ2;
}
part Context1 {
    part 'System 1';
}
package ReqSatisfactions {
    private import Requirements::*;
    satisfy Req1 by Context1.'System 1';
    satisfy Req1 by Context1;
    satisfy requirement Req2 : REQ2 by Context1.'System 1';
}
