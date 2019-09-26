package org.endeavourhealth.common.security.ddsmodel.models.json;

public class JsonDDSOrganisationStatus {
    private String practiceName = null;
    private String odsCode = null;
    private String ccg = null;
    private String referenceAgreement = null;
    private String lastReceived;
    private Boolean inError = false;

    public JsonDDSOrganisationStatus() {
    }

    public String getOdsCode() {
        return odsCode;
    }

    public void setOdsCode(String odsCode) {
        this.odsCode = odsCode;
    }

    public String getLastReceived() {
        return lastReceived;
    }

    public void setLastReceived(String lastReceived) {
        this.lastReceived = lastReceived;
    }

    public Boolean getInError() {
        return inError;
    }

    public void setInError(Boolean inError) {
        this.inError = inError;
    }

    public String getPracticeName() {
        return practiceName;
    }

    public void setPracticeName(String practiceName) {
        this.practiceName = practiceName;
    }

    public String getCcg() {
        return ccg;
    }

    public void setCcg(String ccg) {
        this.ccg = ccg;
    }

    public String getReferenceAgreement() {
        return referenceAgreement;
    }

    public void setReferenceAgreement(String referenceAgreement) {
        this.referenceAgreement = referenceAgreement;
    }
}
