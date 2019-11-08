package org.endeavourhealth.common.security.datasharingmanagermodel.models.enums;


public enum MapType {
    SERVICE((short)0),
    ORGANISATION((short)1),
    REGION((short)2),
    DATASHARINGAGREEMENT((short)3),
    DATAFLOW((short)4),
    DATAPROCESSINGAGREEMENT((short)5),
    COHORT((short)6),
    DATASET((short)7),
    PUBLISHER((short)8),
    SUBSCRIBER((short)9),
    PURPOSE((short)10),
    BENEFIT((short)11),
    DOCUMENT((short)12),
    DATAEXCHANGE((short)13),
    PROJECT((short)14),
    EXTRACTTECHNICALDETAILS((short)15),
    SCHEDULE((short)16);

    private Short mapType;

    MapType(short mapType) {
        this.mapType = mapType;
    }

    public Short getMapType() {
        return mapType;
    }
}
