#refinement dependency engine4Cyl to VehicleConfiguration_b::PartsTree::vehicle_b::engine;
doc description /* Note. */
comment /* Note. */
#Security #Classified metadata Classified {
	classificationLevel = ClassificationLevel::secret;
}
alias Car for Automobile {
	/* Note. */
}
language "English"
/* Note. */
dependency Use from 'Application Layer' to 'Service Layer';
dependency z to x, y;
abstract metadata def ControllingMeasureMetadata :> SemanticMetadata {
}
rep inOCL language "ocl"
/* Note. */
alias <soi> systemOfInterest for theSystem;
metadata prompt : Tag about target {
	values = ("One", "Two",);
	text = "First line.
Second line.";
}
package MetadataAbout {
	part vehicle {
		part interior {
			part alarm;
		}
		part bodyAssy {
			part keylessEntry;
		}
	}
	metadata SecurityFeature about vehicle::interior::alarm, vehicle::bodyAssy::keylessEntry;
}
