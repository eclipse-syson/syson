// Covers declared and alias names that start and end with an apostrophe, which belongs to the name
// (SysMLNameFormatter#declaredName, MembershipRenderer)
package QuotedNamesWithApostrophes {
    part '\'x\'';
    part def '\'T\'' {
        attribute <'\'s\''> '\'y\'';
    }
    part q : '\'T\'';
    alias '\'al\'' for '\'x\'';
}
