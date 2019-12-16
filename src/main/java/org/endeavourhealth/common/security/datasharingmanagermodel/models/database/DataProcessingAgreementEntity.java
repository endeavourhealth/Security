package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonDPA;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "data_processing_agreement", schema = "data_sharing_manager")
public class DataProcessingAgreementEntity {
    private String uuid;
    private String name;
    private String description;
    private String derivation;
    private String publisherInformation;
    private String publisherContractInformation;
    private String publisherDataset;
    private short dsaStatusId;
    private String returnToSenderPolicy;
    private Date startDate;
    private Date endDate;
    @Transient private List<String> purposes;
    @Transient private List<String> benefits;
    @Transient private List<String> regions;
    @Transient private List<String> publishers;
    @Transient private List<String> documentation;

    public DataProcessingAgreementEntity() {
    }

    public DataProcessingAgreementEntity(JsonDPA dpa) {
        this.uuid = dpa.getUuid();
        this.name = dpa.getName();
        this.description = dpa.getDescription();
        this.derivation = dpa.getDerivation();
        this.publisherInformation = dpa.getPublisherInformation();
        this.publisherContractInformation = dpa.getPublisherContractInformation();
        this.publisherDataset = dpa.getPublisherDataset();
        this.dsaStatusId = dpa.getDsaStatusId();
        this.returnToSenderPolicy = dpa.getReturnToSenderPolicy();
        if (dpa.getStartDate() != null) {
            this.startDate = Date.valueOf(dpa.getStartDate());
        }
        if (dpa.getEndDate() != null) {
            this.endDate = Date.valueOf(dpa.getEndDate());
        }
        this.purposes = new ArrayList<>();
        dpa.getPurposes().forEach((p) -> this.purposes.add(p.getUuid()));
        this.benefits = new ArrayList<>();
        dpa.getBenefits().forEach((b) -> this.benefits.add(b.getUuid()));
        this.regions = new ArrayList<>();
        dpa.getRegions().forEach((k, v) -> this.regions.add(k.toString()));
        this.publishers = new ArrayList<>();
        dpa.getPublishers().forEach((k, v) -> this.publishers.add(k.toString()));
        this.documentation = new ArrayList<>();
        dpa.getDocumentations().forEach((d) -> this.documentation.add(d.getUuid()));
    }

    @Id
    @Column(name = "uuid", nullable = false, length = 36)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 10000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "derivation", nullable = true, length = 100)
    public String getDerivation() {
        return derivation;
    }

    public void setDerivation(String derivation) {
        this.derivation = derivation;
    }

    @Basic
    @Column(name = "publisher_information", nullable = true, length = 100)
    public String getPublisherInformation() {
        return publisherInformation;
    }

    public void setPublisherInformation(String publisherInformation) {
        this.publisherInformation = publisherInformation;
    }

    @Basic
    @Column(name = "publisher_contract_information", nullable = true, length = 100)
    public String getPublisherContractInformation() {
        return publisherContractInformation;
    }

    public void setPublisherContractInformation(String publisherContractInformation) {
        this.publisherContractInformation = publisherContractInformation;
    }

    @Basic
    @Column(name = "publisher_dataset", nullable = true, length = 36)
    public String getPublisherDataset() {
        return publisherDataset;
    }

    public void setPublisherDataset(String publisherDataset) {
        this.publisherDataset = publisherDataset;
    }

    @Basic
    @Column(name = "dsa_status_id", nullable = false)
    public short getDsaStatusId() {
        return dsaStatusId;
    }

    public void setDsaStatusId(short dsaStatusId) {
        this.dsaStatusId = dsaStatusId;
    }

    @Basic
    @Column(name = "return_to_sender_policy", nullable = true, length = 100)
    public String getReturnToSenderPolicy() {
        return returnToSenderPolicy;
    }

    public void setReturnToSenderPolicy(String returnToSenderPolicy) {
        this.returnToSenderPolicy = returnToSenderPolicy;
    }

    @Basic
    @Column(name = "start_date", nullable = true)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "end_date", nullable = true)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Transient
    public List<String> getPurposes() {
        return purposes;
    }

    @Transient
    public void setPurposes(List<String> purposes) {
        this.purposes = purposes;
    }

    @Transient
    public List<String> getBenefits() {
        return benefits;
    }

    @Transient
    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

    @Transient
    public List<String> getRegions() {
        return regions;
    }

    @Transient
    public void setRegions(List<String> regions) {
        this.regions = regions;
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
    public List<String> getDocumentation() {
        return documentation;
    }

    @Transient
    public void setDocumentation(List<String> documentation) {
        this.documentation = documentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataProcessingAgreementEntity that = (DataProcessingAgreementEntity) o;

        if (dsaStatusId != that.dsaStatusId) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (derivation != null ? !derivation.equals(that.derivation) : that.derivation != null) return false;
        if (publisherInformation != null ? !publisherInformation.equals(that.publisherInformation) : that.publisherInformation != null)
            return false;
        if (publisherContractInformation != null ? !publisherContractInformation.equals(that.publisherContractInformation) : that.publisherContractInformation != null)
            return false;
        if (publisherDataset != null ? !publisherDataset.equals(that.publisherDataset) : that.publisherDataset != null)
            return false;
        if (returnToSenderPolicy != null ? !returnToSenderPolicy.equals(that.returnToSenderPolicy) : that.returnToSenderPolicy != null)
            return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;

        return true;
        // N.B. Ignores linked items for now
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (derivation != null ? derivation.hashCode() : 0);
        result = 31 * result + (publisherInformation != null ? publisherInformation.hashCode() : 0);
        result = 31 * result + (publisherContractInformation != null ? publisherContractInformation.hashCode() : 0);
        result = 31 * result + (publisherDataset != null ? publisherDataset.hashCode() : 0);
        result = 31 * result + (int) dsaStatusId;
        result = 31 * result + (returnToSenderPolicy != null ? returnToSenderPolicy.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
        // N.B. Definition excludes linked items for now
    }
}
