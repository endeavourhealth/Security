package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "extract_technical_details", schema = "data_sharing_manager")
public class ExtractTechnicalDetailsEntity {

    private String uuid;
    private String name;
    private String sftpHostName;
    private String sftpHostDirectory;
    private String sftpHostPort;
    private String sftpClientUsername;
    private String sftpClientPrivateKeyPassword;
    private String sftpHostPublicKeyFilename;
    private String sftpHostPublicKeyFileData;

    // TODO (for now, a subset of all required fields, while doing further development)
    /*
    private String sftpClientPrivateKeyFilename;
    private String sftpClientPrivateKeyFileData;
    private String pgpCustomerPublicKeyFilename;
    private String pgpCustomerPublicKeyFileData;
    private String pgpInternalPublicKeyFilename;
    private String pgpInternalPublicKeyFileData;
    */

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
    @Column(name = "sftp_host_name")
    public String getSftpHostName() {
        return sftpHostName;
    }

    public void setSftpHostName(String sftpHostName) {
        this.sftpHostName = sftpHostName;
    }

    @Basic
    @Column(name = "sftp_host_directory")
    public String getSftpHostDirectory() {
        return sftpHostDirectory;
    }

    public void setSftpHostDirectory(String sftpHostDirectory) {
        this.sftpHostDirectory = sftpHostDirectory;
    }

    @Basic
    @Column(name = "sftp_host_port")
    public String getSftpHostPort() {
        return sftpHostPort;
    }

    public void setSftpHostPort(String sftpHostPort) {
        this.sftpHostPort = sftpHostPort;
    }

    @Basic
    @Column(name = "sftp_client_username")
    public String getSftpClientUsername() {
        return sftpClientUsername;
    }

    public void setSftpClientUsername(String sftpClientUsername) {
        this.sftpClientUsername = sftpClientUsername;
    }

    @Basic
    @Column(name = "sftp_client_private_key_password")
    public String getSftpClientPrivateKeyPassword() {
        return sftpClientPrivateKeyPassword;
    }

    public void setSftpClientPrivateKeyPassword(String sftpClientPrivateKeyPassword) {
        this.sftpClientPrivateKeyPassword = sftpClientPrivateKeyPassword;
    }

    @Basic
    @Column(name = "sftp_host_public_key_filename")
    public String getSftpHostPublicKeyFilename() {
        return sftpHostPublicKeyFilename;
    }

    public void setSftpHostPublicKeyFilename(String sftpHostPublicKeyFilename) {
        this.sftpHostPublicKeyFilename = sftpHostPublicKeyFilename;
    }

    @Basic
    @Column(name = "sftp_host_public_key_fileData")
    public String getSftpHostPublicKeyFileData() {
        return sftpHostPublicKeyFileData;
    }

    public void setSftpHostPublicKeyFileData(String sftpHostPublicKeyFileData) {
        this.sftpHostPublicKeyFileData = sftpHostPublicKeyFileData;
    }

    /*

    @Basic
    @Column(name = "sftp_client_private_key")
    public String getSftpClientPrivateKey() {
        return sftpClientPrivateKey;
    }

    public void setSftpClientPrivateKey(String sftpClientPrivateKey) {
        this.sftpClientPrivateKey = sftpClientPrivateKey;
    }

    @Basic
    @Column(name = "pgp_customer_public_key")
    public String getPgpCustomerPublicKey() {
        return pgpCustomerPublicKey;
    }

    public void setPgpCustomerPublicKey(String pgpCustomerPublicKey) {
        this.pgpCustomerPublicKey = pgpCustomerPublicKey;
    }

    @Basic
    @Column(name = "pgp_internal_public_key")
    public String getPgpArchivePublicKey() {
        return pgpArchivePublicKey;
    }

    public void setPgpInternalPublicKey(String pgpInternalPublicKey) {
        this.pgpInternalPublicKey = pgpInternalPublicKey;
    }

    */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractTechnicalDetailsEntity that = (ExtractTechnicalDetailsEntity) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sftpHostName, that.sftpHostName) &&
                Objects.equals(sftpHostDirectory, that.sftpHostDirectory) &&
                Objects.equals(sftpHostPort, that.sftpHostPort) &&
                Objects.equals(sftpClientUsername, that.sftpClientUsername) &&
                Objects.equals(sftpClientPrivateKeyPassword, that.sftpClientPrivateKeyPassword) &&
                Objects.equals(sftpHostPublicKeyFilename, that.sftpHostPublicKeyFilename) &&
                Objects.equals(sftpHostPublicKeyFileData, that.sftpHostPublicKeyFileData)
                /*
                &&
                Objects.equals(sftpClientPrivateKey, that.sftpClientPrivateKey) &&
                Objects.equals(pgpCustomerPublicKey, that.pgpCustomerPublicKey) &&
                Objects.equals(pgpInternalPublicKey, that.pgpInternalPublicKey)
                */
                ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, name, sftpHostName, sftpHostDirectory, sftpHostPort,
                sftpClientUsername, sftpClientPrivateKeyPassword, sftpHostPublicKeyFilename, sftpHostPublicKeyFileData
                /*sftpClientPrivateKey, pgpCustomerPublicKey, pgpInternalPublicKey*/);
    }

}
