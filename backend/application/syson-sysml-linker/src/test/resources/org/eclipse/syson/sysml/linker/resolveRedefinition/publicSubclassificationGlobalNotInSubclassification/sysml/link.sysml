attribute a {

}

part def part1 {
  public attribute attribute1;
}

part def part2 {
  attribute redefinedAttr1 :>> $::part1::attribute1 :> a;
}
