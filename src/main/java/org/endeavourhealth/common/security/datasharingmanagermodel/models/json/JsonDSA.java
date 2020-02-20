package org.endeavourhealth.common.security.datasharingmanagermodel.models.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonDSA {
    private String uuid = null;
    private String name = null;
    private String description = null;
    private String derivation = null;
    private Short dsaStatusId = null;
    private Short consentModelId = null;
    private String startDate = null;
    private String endDate = null;
    private Map<UUID, String> dataFlows = null;
    private Map<UUID, String> regions = null;
    private Map<UUID, String> publishers = null;
    private Map<UUID, String> subscribers = null;
    private Map<UUID, String> projects = null;
    private List<JsonDocumentation> documentations = null;
    private List<JsonPurpose> purposes = null;
    private List<JsonPurpose> benefits = null;
    private Map<UUID, String> cohorts = null;
    private Map<UUID, String> dataSets = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDerivation() {
        return derivation;
    }

    public void setDerivation(String derivation) {
        this.derivation = derivation;
    }

    public Short getDsaStatusId() {
        return dsaStatusId;
    }

    public void setDsaStatusId(Short dsaStatusId) {
        this.dsaStatusId = dsaStatusId;
    }

    public Short getConsentModelId() {
        return consentModelId;
    }

    public void setConsentModelId(Short consentModelId) {
        this.consentModelId = consentModelId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<UUID, String> getDataFlows() {
        return dataFlows;
    }

    public void setDataFlows(Map<UUID, String> dataFlows) {
        this.dataFlows = dataFlows;
    }

    public Map<UUID, String> getRegions() {
        return regions;
    }

    public void setRegions(Map<UUID, String> regions) {
        this.regions = regions;
    }

    public Map<UUID, String> getPublishers() {
        return publishers;
    }

    public void setPublishers(Map<UUID, String> publishers) {
        this.publishers = publishers;
    }

    public Map<UUID, String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Map<UUID, String> subscribers) {
        this.subscribers = subscribers;
    }

    public List<JsonPurpose> getPurposes() {
        return purposes;
    }

    public void setPurposes(List<JsonPurpose> purposes) {
        this.purposes = purposes;
    }

    public List<JsonPurpose> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<JsonPurpose> benefits) {
        this.benefits = benefits;
    }

    public List<JsonDocumentation> getDocumentations() {
        return documentations;
    }

    public void setDocumentations(List<JsonDocumentation> documentations) {
        this.documentations = documentations;
    }

    public Map<UUID, String> getProjects() {
        return projects;
    }

    public void setProjects(Map<UUID, String> projects) {
        this.projects = projects;
    }

    public Map<UUID, String> getCohorts() {
        return cohorts;
    }

    public void setCohorts(Map<UUID, String> cohorts) {
        this.cohorts = cohorts;
    }

    public Map<UUID, String> getDataSets() {
        return dataSets;
    }

    public void setDataSets(Map<UUID, String> dataSets) {
        this.dataSets = dataSets;
    }
}
