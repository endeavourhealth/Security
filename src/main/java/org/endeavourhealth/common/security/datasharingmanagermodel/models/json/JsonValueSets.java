package org.endeavourhealth.common.security.datasharingmanagermodel.models.json;

public class JsonValueSets {

    private Integer id;
    private String name;
    private JsonValueSetCodes[] valuesSetCodes;

    private String read2ConceptIds;
    private String ctv3ConceptIds;
    private String sctConceptIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonValueSetCodes[] getValuesSetCodes() {
        return valuesSetCodes;
    }

    public void setValuesSetCodes(JsonValueSetCodes[] valuesSetCodes) {
        this.valuesSetCodes = valuesSetCodes;
    }

    public String getRead2ConceptIds() {
        return read2ConceptIds;
    }

    public void setRead2ConceptIds(String read2ConceptIds) {
        this.read2ConceptIds = read2ConceptIds;
    }

    public String getCtv3ConceptIds() {
        return ctv3ConceptIds;
    }

    public void setCtv3ConceptIds(String ctv3ConceptIds) {
        this.ctv3ConceptIds = ctv3ConceptIds;
    }

    public String getSctConceptIds() {
        return sctConceptIds;
    }

    public void setSctConceptIds(String sctConceptIds) {
        this.sctConceptIds = sctConceptIds;
    }
}
