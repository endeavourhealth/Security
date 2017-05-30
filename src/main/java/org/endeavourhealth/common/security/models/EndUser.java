package org.endeavourhealth.common.security.models;

import java.util.UUID;

public class EndUser {
    private UUID id;
    private String title;
    private String forename;
    private String surname;
    private String email;
    private Boolean isSuperUser;

    public EndUser() {
    }

    public EndUser(UUID id, String title, String forename, String surname, String email, Boolean isSuperUser) {
        this.id = id;
        this.title = title;
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.isSuperUser = isSuperUser;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsSuperUser() {
        return isSuperUser;
    }

    public void setIsSuperUser(Boolean isSuperUser) {
        this.isSuperUser = isSuperUser;
    }

    @Override
    public String toString() {
        return "EndUser{" +
                "id=" + id +
                ", forename='" + forename + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}