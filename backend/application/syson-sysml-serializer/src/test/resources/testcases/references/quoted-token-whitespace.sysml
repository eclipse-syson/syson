// Covers whitespace inside unrestricted names and strings, which the text comparison must not ignore (NameRenderer)
package QuotedTokenWhitespace {
    part def 'two  spaces';
    part def ' padded ';
    part p : 'two  spaces';
    part q : ' padded ';
    attribute s = "a  b";
    attribute t = " lead and trail ";
    attribute u = 'two  spaces'::x;
    comment c locale "en  US" /* body */
}
