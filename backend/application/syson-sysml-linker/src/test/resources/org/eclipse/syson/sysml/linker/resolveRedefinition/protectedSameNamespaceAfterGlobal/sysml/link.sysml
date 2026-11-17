attribute a {

}

part def part2 {
  attribute redefinedAttr1 :>> $::part2::attribute1 :> a;
  protected attribute attribute1;
}
