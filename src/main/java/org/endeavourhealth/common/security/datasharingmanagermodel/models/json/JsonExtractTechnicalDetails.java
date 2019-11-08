package org.endeavourhealth.common.security.datasharingmanagermodel.models.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonExtractTechnicalDetails {

    private String uuid = null;
    private String name = null;
    private String sftpHostName = null;

    // TODO (for now, just a subset of all the required fields, while doing development)
    /*
    private String sftpHostPublicKey = null;
    private String sftpHostDirectory = null;
    private Integer sftpHostPort = null;
    private String sftpClientUsername = null;
    private String sftpClientPrivateKeyPassword = null;
    private String sftpClientPrivateKey = null;
    private String pgpCustomerPublicKey = null;
    private String pgpArchivePublicKey = null;
    private String pgpArchivePrivateKey = null;
    */

    public JsonExtractTechnicalDetails() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSftpHostName() {
        return sftpHostName;
    }

    public void setSftpHostName(String sftpHostName) {
        this.sftpHostName = sftpHostName;
    }

    /*
    public String getSftpHostPublicKey() {
        return sftpHostPublicKey;
    }

    public void setSftpHostPublicKey(String sftpHostPublicKey) {
        this.sftpHostPublicKey = sftpHostPublicKey;
    }

    public String getSftpHostDirectory() {
        return sftpHostDirectory;
    }

    public void setSftpHostDirectory(String sftpHostDirectory) {
        this.sftpHostDirectory = sftpHostDirectory;
    }

    public Integer getSftpHostPort() {
        return sftpHostPort;
    }

    public void setSftpHostPort(Integer sftpHostPort) {
        this.sftpHostPort = sftpHostPort;
    }

    public String getSftpClientUsername() {
        return sftpClientUsername;
    }

    public void setSftpClientUsername(String sftpClientUsername) {
        this.sftpClientUsername = sftpClientUsername;
    }

    public String getSftpClientPrivateKeyPassword() {
        return sftpClientPrivateKeyPassword;
    }

    public void setSftpClientPrivateKeyPassword(String sftpClientPrivateKeyPassword) {
        this.sftpClientPrivateKeyPassword = sftpClientPrivateKeyPassword;
    }

    public String getSftpClientPrivateKey() {
        return sftpClientPrivateKey;
    }

    public void setSftpClientPrivateKey(String sftpClientPrivateKey) {
        this.sftpClientPrivateKey = sftpClientPrivateKey;
    }

    public String getPgpCustomerPublicKey() {
        return pgpCustomerPublicKey;
    }

    public void setPgpCustomerPublicKey(String pgpCustomerPublicKey) {
        this.pgpCustomerPublicKey = pgpCustomerPublicKey;
    }

    public String getPgpArchivePublicKey() {
        return pgpArchivePublicKey;
    }

    public void setPgpArchivePublicKey(String pgpArchivePublicKey) {
        this.pgpArchivePublicKey = pgpArchivePublicKey;
    }

    public String getPgpArchivePrivateKey() {
        return pgpArchivePrivateKey;
    }

    public void setPgpArchivePrivateKey(String pgpArchivePrivateKey) {
        this.pgpArchivePrivateKey = pgpArchivePrivateKey;
    }
    */

}