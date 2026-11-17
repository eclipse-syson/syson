package FollowingSegmentVisibility {
    part def Leaf;
    part def Container;
    part def Holder;

    part holder : Holder {
        public part container : Container {
            public part publicPart : Leaf;
            private part privatePart : Leaf;
            protected part protectedPart : Leaf;
        }
    }

    // SysML 7.6.6 allows only public memberships after the first feature-chain segment.
    ref publicReference : Leaf = holder.container.publicPart;
    ref privateReference : Leaf = holder.container.privatePart;
    ref protectedReference : Leaf = holder.container.protectedPart;
}
