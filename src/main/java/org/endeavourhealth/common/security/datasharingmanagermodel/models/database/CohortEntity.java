package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cohort", schema = "data_sharing_manager")
public class CohortEntity {
    private String uuid;
    private String name;
    private Short consentModelId;
    private String description;
    private String technicalDefinition;

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
    @Column(name = "consent_model_id")
    public Short getConsentModelId() {
        return consentModelId;
    }

    public void setConsentModelId(Short consentModelId) {
        this.consentModelId = consentModelId;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CohortEntity that = (CohortEntity) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(consentModelId, that.consentModelId) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, name, consentModelId, description);
    }

    @Basic
    @Column(name = "technical_definition")
    public String getTechnicalDefinition() {
        return technicalDefinition;
    }

    public void setTechnicalDefinition(String technicalDefinition) {
        this.technicalDefinition = technicalDefinition;
    }
}
