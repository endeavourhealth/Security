package org.endeavourhealth.common.security.datasharingmanagermodel.models.json;

public class JsonOrganisationCCG {
    private String odsCode = null;
    private String ccgName = null;

    public JsonOrganisationCCG() {
    }

    public String getOdsCode() {
        return odsCode;
    }

    public void setOdsCode(String odsCode) {
        this.odsCode = odsCode;
    }

    public String getCcgName() {
        return ccgName;
    }

    public void setCcgName(String ccgName) {
        this.ccgName = ccgName;
    }
}
