package Package1 {
	view view1 : StandardViewDefinitions::InterconnectionView {
		expose part1;
		expose part1::part2;
	}
	part part1 {
		part part2;
	}
}