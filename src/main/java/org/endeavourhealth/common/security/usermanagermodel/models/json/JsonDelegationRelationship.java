package org.endeavourhealth.common.security.usermanagermodel.models.json;

import org.endeavourhealth.common.security.usermanagermodel.models.database.DelegationRelationshipEntity;

public class JsonDelegationRelationship {
    private String uuid = null;
    private String parentUuid = null;
    private short parentType;
    private String childUuid = null;
    private short childType;
    private boolean includeAllChildren;
    private boolean createSuperUsers;
    private boolean createUsers;
    private String delegation = null;
    private boolean isDeleted;

    public JsonDelegationRelationship() {
    }

    public JsonDelegationRelationship(DelegationRelationshipEntity relationshipEntity) {
        this.uuid = relationshipEntity.getUuid();
        this.parentUuid = relationshipEntity.getParentUuid();
        this.parentType = relationshipEntity.getParentType();
        this.childUuid = relationshipEntity.getChildUuid();
        this.childType = relationshipEntity.getChildType();
        this.includeAllChildren = relationshipEntity.getIncludeAllChildren() == 1;
        this.createSuperUsers = relationshipEntity.getCreateSuperUsers() == 1;
        this.createUsers = relationshipEntity.getCreateUsers() == 1;
        this.delegation = relationshipEntity.getDelegation();
        this.isDeleted = relationshipEntity.getIsDeleted() == 1;

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public short getParentType() {
        return parentType;
    }

    public void setParentType(short parentType) {
        this.parentType = parentType;
    }

    public String getChildUuid() {
        return childUuid;
    }

    public void setChildUuid(String childUuid) {
        this.childUuid = childUuid;
    }

    public short getChildType() {
        return childType;
    }

    public void setChildType(short childType) {
        this.childType = childType;
    }

    public boolean isIncludeAllChildren() {
        return includeAllChildren;
    }

    public void setIncludeAllChildren(boolean includeAllChildren) {
        this.includeAllChildren = includeAllChildren;
    }

    public boolean isCreateSuperUsers() {
        return createSuperUsers;
    }

    public void setCreateSuperUsers(boolean createSuperUsers) {
        this.createSuperUsers = createSuperUsers;
    }

    public boolean isCreateUsers() {
        return createUsers;
    }

    public void setCreateUsers(boolean createUsers) {
        this.createUsers = createUsers;
    }

    public String getDelegation() {
        return delegation;
    }

    public void setDelegation(String delegation) {
        this.delegation = delegation;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
