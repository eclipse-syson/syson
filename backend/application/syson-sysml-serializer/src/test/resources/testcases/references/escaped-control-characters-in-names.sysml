// Covers unrestricted names holding the control characters that the grammar escapes as \t, \n and \r; \b and \f
// are not valid in the XMI the round-trip tests compare (NameEscaper#escapeUnrestrictedName, NameEscaperTest)
package EscapedControlCharacters {
    part def 'a\nb';
    part def 'tab\there';
    part def 'x\ry';
    part p : 'a\nb';
    part '\'q\'\n' : 'tab\there';
}
