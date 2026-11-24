// Covers quoted names containing index syntax kept quoted in references (SysMLNameFormatter#referenceName)
package UnrestrictedNameWithIndexSyntax {
    part def 'a#(1)';
    part p : 'a#(1)';
    part q : UnrestrictedNameWithIndexSyntax::'a#(1)';
}
