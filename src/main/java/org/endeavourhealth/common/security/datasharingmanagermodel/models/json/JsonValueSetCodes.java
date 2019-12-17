package org.endeavourhealth.common.security.datasharingmanagermodel.models.json;

public class JsonValueSetCodes {

    private Integer valueSetId;
    private String read2ConceptId;
    private String ctv3ConceptId;
    private String sctConceptId;

    public int getValueSetId() {
        return valueSetId;
    }

    public void setValueSetId(int valueSetId) {
        this.valueSetId = valueSetId;
    }

    public String getRead2ConceptId() {
        return read2ConceptId;
    }

    public void setRead2ConceptId(String read2ConceptId) {
        this.read2ConceptId = read2ConceptId;
    }

    public String getCtv3ConceptId() {
        return ctv3ConceptId;
    }

    public void setCtv3ConceptId(String ctv3ConceptId) {
        this.ctv3ConceptId = ctv3ConceptId;
    }

    public String getSctConceptId() {
        return sctConceptId;
    }

    public void setSctConceptId(String sctConceptId) {
        this.sctConceptId = sctConceptId;
    }
}
