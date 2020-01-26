package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityMasterMappingDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.enums.MapType;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonDataSet;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dataset", schema = "data_sharing_manager")
public class DatasetEntity {
    private String uuid;
    private String name;
    private String description;
    private String technicalDefinition;
    @Transient private List<String> dpas;

    public DatasetEntity() {
    }

    public DatasetEntity(JsonDataSet dataSet) {
        updateFromJson(dataSet);
    }

    public void updateFromJson(JsonDataSet dataSet) {
        this.uuid = dataSet.getUuid();
        this.name = dataSet.getName();
        this.description = dataSet.getDescription();
        this.technicalDefinition = dataSet.getTechnicalDefinition();
        this.dpas = new ArrayList<>();
        if (dataSet.getDpas() != null) {
            dataSet.getDpas().forEach((k, v) -> this.dpas.add(k.toString()));
        }
    }

    public void setMappingsFromDAL () throws Exception {
        SecurityMasterMappingDAL securityMasterMappingDAL = new SecurityMasterMappingDAL();
        Short thisMapType = MapType.DATASET.getMapType();

        this.setDpas(securityMasterMappingDAL.getParentMappings(this.uuid, thisMapType, MapType.DATAPROCESSINGAGREEMENT.getMapType()));
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
        DatasetEntity that = (DatasetEntity) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, name, description);
    }

    @Basic
    @Column(name = "technical_definition")
    public String getTechnicalDefinition() {
        return technicalDefinition;
    }

    public void setTechnicalDefinition(String technicalDefinition) {
        this.technicalDefinition = technicalDefinition;
    }

    @Transient
    public List<String> getDpas() {
        return dpas;
    }

    @Transient
    public void setDpas(List<String> dpas) {
        this.dpas = dpas;
    }
}
