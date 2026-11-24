// Covers binding declaration specializations, short name, user keyword, references keyword and body (ConnectorRenderer)
package BindingConnectorDeclarations {
    metadata def M;
    part a;
    part b;
    binding x bind a = b;
    binding bb :> x bind a = b;
    binding <s> bs bind a = b;
    #M bind a = b;
    bind n references a = b;
    bind a = b { part q; }
}
