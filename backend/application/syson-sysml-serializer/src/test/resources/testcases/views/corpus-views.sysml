view def NestedView;
view vehiclePartsTree_Safety : PartsTreeView {
	satisfy requirement sv : SafetyViewpoint;
	expose PartsTree::**;
	filter @Safety;
}
view vehicleMandatorySafetyFeatureView :> vehicleSafetyFeatureView {
	expose vehicle::*::**;
}
view vehicleMandatorySafetyFeatureViewStandalone {
	expose vehicle::**[@Safety and Safety::isMandatory];
}
