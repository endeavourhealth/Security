package org.endeavourhealth.common.security.datasharingmanagermodel.models.json;

import java.util.UUID;

public abstract class JsonItem {
    private String uuid = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setUuidIfRequired() {
        if (this.getUuid() == null) {
            this.setUuid(UUID.randomUUID().toString());
        }
    }

}
