package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import javax.persistence.*;

@Entity
@Table(name = "master_mapping", schema = "data_sharing_manager")
@IdClass(MasterMappingEntityPK.class)
public class MasterMappingEntity {
    private String childUuid;
    private short childMapTypeId;
    private String parentUuid;
    private short parentMapTypeId;
    private byte isDefault;

    @Id
    @Column(name = "child_uuid", nullable = false, length = 36)
    public String getChildUuid() {
        return childUuid;
    }

    public void setChildUuid(String childUuid) {
        this.childUuid = childUuid;
    }

    @Id
    @Column(name = "child_map_type_id", nullable = false)
    public short getChildMapTypeId() {
        return childMapTypeId;
    }

    public void setChildMapTypeId(short childMapTypeId) {
        this.childMapTypeId = childMapTypeId;
    }

    @Id
    @Column(name = "parent_uuid", nullable = false, length = 36)
    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    @Id
    @Column(name = "parent_map_type_id", nullable = false)
    public short getParentMapTypeId() {
        return parentMapTypeId;
    }

    public void setParentMapTypeId(short parentMapTypeId) {
        this.parentMapTypeId = parentMapTypeId;
    }

    @Basic
    @Column(name = "is_default", nullable = false)
    public byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(byte isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MasterMappingEntity that = (MasterMappingEntity) o;

        if (childMapTypeId != that.childMapTypeId) return false;
        if (parentMapTypeId != that.parentMapTypeId) return false;
        if (isDefault != that.isDefault) return false;
        if (childUuid != null ? !childUuid.equals(that.childUuid) : that.childUuid != null) return false;
        if (parentUuid != null ? !parentUuid.equals(that.parentUuid) : that.parentUuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = childUuid != null ? childUuid.hashCode() : 0;
        result = 31 * result + (int) childMapTypeId;
        result = 31 * result + (parentUuid != null ? parentUuid.hashCode() : 0);
        result = 31 * result + (int) parentMapTypeId;
        result = 31 * result + (int) isDefault;
        return result;
    }
}
