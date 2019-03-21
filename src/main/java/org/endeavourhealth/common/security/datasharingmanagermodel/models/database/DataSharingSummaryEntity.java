package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import javax.persistence.*;
import java.sql.Date;

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getDataSharingSummaryStatistics",
                procedureName = "getDataSharingSummaryStatistics"
        ),
        @NamedStoredProcedureQuery(
                name = "getDataProcessingAgreementStatistics",
                procedureName = "getDataProcessingAgreementStatistics"
        ),
        @NamedStoredProcedureQuery(
                name = "getDataSharingAgreementStatistics",
                procedureName = "getDataSharingAgreementStatistics"
        ),
        @NamedStoredProcedureQuery(
                name = "getDataFlowStatistics",
                procedureName = "getDataFlowStatistics"
        ),
        @NamedStoredProcedureQuery(
                name = "getCohortStatistics",
                procedureName = "getCohortStatistics"
        ),
        @NamedStoredProcedureQuery(
                name = "getDatasetStatistics",
                procedureName = "getDatasetStatistics"
        )
})
@Entity
@Table(name = "data_sharing_summary", schema = "data_sharing_manager")
public class DataSharingSummaryEntity {
    private String uuid;
    private String name;
    private String description;
    private String purpose;
    private short natureOfInformationId;
    private String schedule2Condition;
    private String benefitToSharing;
    private String overviewOfDataItems;
    private short formatTypeId;
    private short dataSubjectTypeId;
    private String natureOfPersonsAccessingData;
    private short reviewCycleId;
    private Date reviewDate;
    private Date startDate;
    private String evidenceOfAgreement;

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
    @Column(name = "purpose", nullable = true, length = 10000)
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Basic
    @Column(name = "nature_of_information_id", nullable = false)
    public short getNatureOfInformationId() {
        return natureOfInformationId;
    }

    public void setNatureOfInformationId(short natureOfInformationId) {
        this.natureOfInformationId = natureOfInformationId;
    }

    @Basic
    @Column(name = "schedule2_condition", nullable = true, length = 100)
    public String getSchedule2Condition() {
        return schedule2Condition;
    }

    public void setSchedule2Condition(String schedule2Condition) {
        this.schedule2Condition = schedule2Condition;
    }

    @Basic
    @Column(name = "benefit_to_sharing", nullable = true, length = 200)
    public String getBenefitToSharing() {
        return benefitToSharing;
    }

    public void setBenefitToSharing(String benefitToSharing) {
        this.benefitToSharing = benefitToSharing;
    }

    @Basic
    @Column(name = "overview_of_data_items", nullable = true, length = 100)
    public String getOverviewOfDataItems() {
        return overviewOfDataItems;
    }

    public void setOverviewOfDataItems(String overviewOfDataItems) {
        this.overviewOfDataItems = overviewOfDataItems;
    }

    @Basic
    @Column(name = "format_type_id", nullable = false)
    public short getFormatTypeId() {
        return formatTypeId;
    }

    public void setFormatTypeId(short formatTypeId) {
        this.formatTypeId = formatTypeId;
    }

    @Basic
    @Column(name = "data_subject_type_id", nullable = false)
    public short getDataSubjectTypeId() {
        return dataSubjectTypeId;
    }

    public void setDataSubjectTypeId(short dataSubjectTypeId) {
        this.dataSubjectTypeId = dataSubjectTypeId;
    }

    @Basic
    @Column(name = "nature_of_persons_accessing_data", nullable = true, length = 100)
    public String getNatureOfPersonsAccessingData() {
        return natureOfPersonsAccessingData;
    }

    public void setNatureOfPersonsAccessingData(String natureOfPersonsAccessingData) {
        this.natureOfPersonsAccessingData = natureOfPersonsAccessingData;
    }

    @Basic
    @Column(name = "review_cycle_id", nullable = false)
    public short getReviewCycleId() {
        return reviewCycleId;
    }

    public void setReviewCycleId(short reviewCycleId) {
        this.reviewCycleId = reviewCycleId;
    }

    @Basic
    @Column(name = "review_date", nullable = true)
    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
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
    @Column(name = "evidence_of_agreement", nullable = true, length = 200)
    public String getEvidenceOfAgreement() {
        return evidenceOfAgreement;
    }

    public void setEvidenceOfAgreement(String evidenceOfAgreement) {
        this.evidenceOfAgreement = evidenceOfAgreement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSharingSummaryEntity that = (DataSharingSummaryEntity) o;

        if (natureOfInformationId != that.natureOfInformationId) return false;
        if (formatTypeId != that.formatTypeId) return false;
        if (dataSubjectTypeId != that.dataSubjectTypeId) return false;
        if (reviewCycleId != that.reviewCycleId) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (purpose != null ? !purpose.equals(that.purpose) : that.purpose != null) return false;
        if (schedule2Condition != null ? !schedule2Condition.equals(that.schedule2Condition) : that.schedule2Condition != null)
            return false;
        if (benefitToSharing != null ? !benefitToSharing.equals(that.benefitToSharing) : that.benefitToSharing != null)
            return false;
        if (overviewOfDataItems != null ? !overviewOfDataItems.equals(that.overviewOfDataItems) : that.overviewOfDataItems != null)
            return false;
        if (natureOfPersonsAccessingData != null ? !natureOfPersonsAccessingData.equals(that.natureOfPersonsAccessingData) : that.natureOfPersonsAccessingData != null)
            return false;
        if (reviewDate != null ? !reviewDate.equals(that.reviewDate) : that.reviewDate != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (evidenceOfAgreement != null ? !evidenceOfAgreement.equals(that.evidenceOfAgreement) : that.evidenceOfAgreement != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (purpose != null ? purpose.hashCode() : 0);
        result = 31 * result + (int) natureOfInformationId;
        result = 31 * result + (schedule2Condition != null ? schedule2Condition.hashCode() : 0);
        result = 31 * result + (benefitToSharing != null ? benefitToSharing.hashCode() : 0);
        result = 31 * result + (overviewOfDataItems != null ? overviewOfDataItems.hashCode() : 0);
        result = 31 * result + (int) formatTypeId;
        result = 31 * result + (int) dataSubjectTypeId;
        result = 31 * result + (natureOfPersonsAccessingData != null ? natureOfPersonsAccessingData.hashCode() : 0);
        result = 31 * result + (int) reviewCycleId;
        result = 31 * result + (reviewDate != null ? reviewDate.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (evidenceOfAgreement != null ? evidenceOfAgreement.hashCode() : 0);
        return result;
    }
}
