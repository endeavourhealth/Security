package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "values_set", schema = "data_sharing_manager")
public class ValueSetsEntity {

    private int id;
    private String name;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String valueSetsName) {
        this.name = valueSetsName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueSetsEntity that = (ValueSetsEntity) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}