package org.endeavourhealth.common.security.ddsmodel.models.database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "exchange_transform_error_state", schema = "audit", catalog = "")
@IdClass(ExchangeTransformErrorStateEntityPK.class)
public class ExchangeTransformErrorStateEntity {
    private String serviceId;
    private String systemId;
    private String exchangeIdsInError;

    @Id
    @Column(name = "service_id")
    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Id
    @Column(name = "system_id")
    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    @Basic
    @Column(name = "exchange_ids_in_error")
    public String getExchangeIdsInError() {
        return exchangeIdsInError;
    }

    public void setExchangeIdsInError(String exchangeIdsInError) {
        this.exchangeIdsInError = exchangeIdsInError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeTransformErrorStateEntity that = (ExchangeTransformErrorStateEntity) o;
        return Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(systemId, that.systemId) &&
                Objects.equals(exchangeIdsInError, that.exchangeIdsInError);
    }

    @Override
    public int hashCode() {

        return Objects.hash(serviceId, systemId, exchangeIdsInError);
    }
}
