attribute a {

}

part def part2 {
  protected attribute attribute1;
  attribute redefinedAttr1 :>> attribute1 :> a;
}
