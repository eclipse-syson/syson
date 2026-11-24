// Covers leading notes of variant, verify and require members written once, before visibility (MembershipSyntaxRenderer)
package PrefixedMemberLeadingNotes {
    variation part def V {
        // note before a declared variant
        variant part v1;
    }
    requirement def R;
    requirement r0 : R;
    verification def W {
        objective {
            // note before a declared verify
            verify requirement r1 : R;
            // note before a private verify
            private verify requirement r2 : R;
            // note before a referencing verify
            verify r0;
        }
    }
    requirement def S {
        // note before a private require
        private require constraint c1 {
            true
        }
        // note before a private assume
        protected assume constraint c2;
    }
}
