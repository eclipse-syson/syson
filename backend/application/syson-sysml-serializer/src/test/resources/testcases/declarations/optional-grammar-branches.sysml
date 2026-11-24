package OptionalGrammarBranches {
    metadata def Tag;
    individual #Tag def Marked;
    event occurrence;

    part left;
    part right;
    part exhibited {
        exhibit state;
    }
    binding bind left = right;
    allocation allocate left to right;
    connection valued = 1 connect left to right;

    action def Steps {
        merge;
        join;
        individual snapshot merge;
        individual timeslice join;
        send left;
    }

    state def Parallel parallel {
        private import OptionalGrammarBranches::left;
        alias alternate for left;
        variant state optionalState;
        then part followingPart;
        entry accept left;
        do action send left;
        state firstState;
        state lastState;
        accept left then lastState;
        action work;
        transition then firstState;
    }
}
