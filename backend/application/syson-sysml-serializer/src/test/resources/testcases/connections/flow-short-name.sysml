// Covers flow short names (FlowRenderer)
package FlowShortName {
    part def P {
        out item o;
        in item i;
    }
    part a : P;
    part b : P;
    flow <s> f from a.o to b.i;
}
