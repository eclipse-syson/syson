// Covers the from keyword required by flow declarations (FlowRenderer)
package FlowDeclarationFrom {
    part def P {
        out item o;
        in item i;
    }
    part a : P;
    part b : P;
    flow x from a.o to b.i;
    flow [1] from a.o to b.i;
    flow :> x from a.o to b.i;
}
