package org.endeavourhealth.common.security.datasharingmanagermodel.models.json;

public abstract class JsonItem {
    private String uuid = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}