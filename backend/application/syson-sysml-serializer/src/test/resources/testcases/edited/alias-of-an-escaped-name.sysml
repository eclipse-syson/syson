// EditedReferenceTargetSerializationTest links the alias, then renames the part to the characters a\nb (a backslash and
// n): the alias spelling 'a\nb' (a line break) no longer names it and must be rewritten (ReferenceNameMatcher)
package U {
    part 'a\nb';
    alias al for 'a\nb';
}
