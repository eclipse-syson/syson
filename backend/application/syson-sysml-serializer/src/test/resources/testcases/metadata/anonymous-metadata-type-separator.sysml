package AnonymousMetadataTypeSeparator {
    metadata def Marker {
        attribute flag : Boolean;
    }

    attribute value {
        metadata : Marker {
            :>> flag = true;
        }
    }
}
