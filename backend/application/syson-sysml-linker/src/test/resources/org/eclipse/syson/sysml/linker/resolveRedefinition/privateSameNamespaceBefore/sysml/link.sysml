attribute a {

}

part def part2 {
  private attribute attribute1;
  attribute redefinedAttr1 :>> attribute1 :> a;
}
