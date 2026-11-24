// A dotted unrestricted name must remain one reference segment after serialization.
package QuotedDottedReference {
	ref item target :> Namespace::Base {
		:>> target = Namespace::'Name.With.Dots';
	}
}
