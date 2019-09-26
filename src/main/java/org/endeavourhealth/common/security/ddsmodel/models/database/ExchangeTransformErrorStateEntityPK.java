package org.endeavourhealth.common.security.ddsmodel.models.database;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ExchangeTransformErrorStateEntityPK implements Serializable {
    private String serviceId;
    private String systemId;

    @Column(name = "service_id")
    @Id
    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Column(name = "system_id")
    @Id
    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeTransformErrorStateEntityPK that = (ExchangeTransformErrorStateEntityPK) o;
        return Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(systemId, that.systemId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(serviceId, systemId);
    }
}
