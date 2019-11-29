package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonProject;

import javax.persistence.*;
import java.sql.Date;
import java.util.*;

@Entity
@Table(name = "project", schema = "data_sharing_manager")
public class ProjectEntity {
    private String uuid;
    private String name;
    private String leadUser;
    private String technicalLeadUser;
    private Short consentModelId;
    private Short deidentificationLevel;
    private Short projectTypeId;
    private Short securityInfrastructureId;
    private String ipAddress;
    private String summary;
    private String businessCase;
    private String objectives;
    private short securityArchitectureId;
    private short storageProtocolId;
    private Short businessCaseStatus;
    private Short flowScheduleId;
    private Short projectStatusId;
    private Date startDate;
    private Date endDate;
    @Transient private List<String> publishers;
    @Transient private List<String> subscribers;
    @Transient private List<String> cohorts;
    @Transient private List<String> dataSets;
    @Transient private List<String> dsas;

    public ProjectEntity() {
    }

    public ProjectEntity(JsonProject project) {
        this.uuid = project.getUuid();
        this.name = project.getName();
        this.leadUser = project.getLeadUser();
        this.technicalLeadUser = project.getTechnicalLeadUser();
        this.consentModelId = project.getConsentModelId();
        this.deidentificationLevel = project.getDeidentificationLevel();
        this.projectTypeId = project.getProjectTypeId();
        this.securityInfrastructureId = project.getSecurityInfrastructureId();
        this.ipAddress = project.getIpAddress();
        this.summary = project.getSummary();
        this.businessCase = project.getBusinessCase();
        this.objectives = project.getObjectives();
        this.securityArchitectureId = project.getSecurityArchitectureId();
        this.storageProtocolId = project.getStorageProtocolId();
        this.businessCaseStatus = project.getBusinessCaseStatus();
        this.flowScheduleId = project.getFlowScheduleId();
        this.projectStatusId = project.getProjectStatusId();
        if (project.getStartDate() != null) {
            this.startDate = Date.valueOf(project.getStartDate());
        }
        if (project.getEndDate() != null) {
            this.endDate = Date.valueOf(project.getEndDate());
        }
    }

    @Id
    @Column(name = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "lead_user")
    public String getLeadUser() {
        return leadUser;
    }

    public void setLeadUser(String leadUser) {
        this.leadUser = leadUser;
    }

    @Basic
    @Column(name = "technical_lead_user")
    public String getTechnicalLeadUser() {
        return technicalLeadUser;
    }

    public void setTechnicalLeadUser(String technicalLeadUser) {
        this.technicalLeadUser = technicalLeadUser;
    }

    @Basic
    @Column(name = "consent_model_id")
    public Short getConsentModelId() {
        return consentModelId;
    }

    public void setConsentModelId(Short consentModelId) {
        this.consentModelId = consentModelId;
    }

    @Basic
    @Column(name = "deidentification_level")
    public Short getDeidentificationLevel() {
        return deidentificationLevel;
    }

    public void setDeidentificationLevel(Short deidentificationLevel) {
        this.deidentificationLevel = deidentificationLevel;
    }

    @Basic
    @Column(name = "project_type_id")
    public Short getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(Short projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    @Basic
    @Column(name = "security_infrastructure_id")
    public Short getSecurityInfrastructureId() {
        return securityInfrastructureId;
    }

    public void setSecurityInfrastructureId(Short securityInfrastructureId) {
        this.securityInfrastructureId = securityInfrastructureId;
    }

    @Basic
    @Column(name = "ip_address")
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Basic
    @Column(name = "summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "business_case")
    public String getBusinessCase() {
        return businessCase;
    }

    public void setBusinessCase(String businessCase) {
        this.businessCase = businessCase;
    }

    @Basic
    @Column(name = "objectives")
    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    @Basic
    @Column(name = "security_architecture_id")
    public short getSecurityArchitectureId() {
        return securityArchitectureId;
    }

    public void setSecurityArchitectureId(short securityArchitectureId) {
        this.securityArchitectureId = securityArchitectureId;
    }

    @Basic
    @Column(name = "storage_protocol_id")
    public short getStorageProtocolId() {
        return storageProtocolId;
    }

    public void setStorageProtocolId(short storageProtocolId) {
        this.storageProtocolId = storageProtocolId;
    }

    @Transient
    public List<String> getPublishers() {
        return publishers;
    }

    @Transient
    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    @Transient
    public List<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<String> subscribers) {
        this.subscribers = subscribers;
    }

    @Transient
    public List<String> getCohorts() {
        return cohorts;
    }

    @Transient
    public void setCohorts(List<String> cohorts) {
        this.cohorts = cohorts;
    }

    @Transient
    public List<String> getDataSets() {
        return dataSets;
    }

    @Transient
    public void setDataSets(List<String> dataSets) {
        this.dataSets = dataSets;
    }

    @Transient
    public List<String> getDsas() {
        return dsas;
    }

    @Transient
    public void setDsas(List<String> dsas) {
        this.dsas = dsas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectEntity that = (ProjectEntity) o;
        return securityArchitectureId == that.securityArchitectureId &&
                storageProtocolId == that.storageProtocolId &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(leadUser, that.leadUser) &&
                Objects.equals(technicalLeadUser, that.technicalLeadUser) &&
                Objects.equals(consentModelId, that.consentModelId) &&
                Objects.equals(deidentificationLevel, that.deidentificationLevel) &&
                Objects.equals(projectTypeId, that.projectTypeId) &&
                Objects.equals(securityInfrastructureId, that.securityInfrastructureId) &&
                Objects.equals(ipAddress, that.ipAddress) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(businessCase, that.businessCase) &&
                Objects.equals(objectives, that.objectives);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, name, leadUser, technicalLeadUser, consentModelId, deidentificationLevel, projectTypeId, securityInfrastructureId, ipAddress, summary, businessCase, objectives, securityArchitectureId, storageProtocolId);
    }

    @Basic
    @Column(name = "business_case_status")
    public Short getBusinessCaseStatus() {
        return businessCaseStatus;
    }

    public void setBusinessCaseStatus(Short businessCaseStatus) {
        this.businessCaseStatus = businessCaseStatus;
    }

    @Basic
    @Column(name = "flow_schedule_id")
    public Short getFlowScheduleId() {
        return flowScheduleId;
    }

    public void setFlowScheduleId(Short flowScheduleId) {
        this.flowScheduleId = flowScheduleId;
    }

    @Basic
    @Column(name = "project_status_id")
    public Short getProjectStatusId() {
        return projectStatusId;
    }

    public void setProjectStatusId(Short projectStatusId) {
        this.projectStatusId = projectStatusId;
    }

    @Basic
    @Column(name = "start_date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "end_date")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
