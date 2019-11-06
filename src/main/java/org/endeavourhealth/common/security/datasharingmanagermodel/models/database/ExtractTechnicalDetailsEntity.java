package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "extract_technical_details", schema = "data_sharing_manager")
public class ExtractTechnicalDetailsEntity {

    private String uuid;
    private String name;
    private String sftpHostName;

    // TODO (for now, just a subset of all the required fields, while doing development
    /*
    private String sftpHostPublicKey;
    private String sftpHostDirectory;
    private Integer sftpHostPort;
    private String sftpClientUsername;
    private String sftpClientPrivateKeyPassword;
    private String sftpClientPrivateKey;
    private String pgpCustomerPublicKey;
    private String pgpArchivePublicKey;
    private String pgpArchivePrivateKey;
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

    /*

    @Basic
    @Column(name = "sftp_host_public_key")
    public String getSftpHostPublicKey() {
        return sftpHostPublicKey;
    }

    public void setSftpHostPublicKey(String sftpHostPublicKey) {
        this.sftpHostPublicKey = sftpHostPublicKey;
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
    public Integer getSftpHostPort() {
        return sftpHostPort;
    }

    public void setSftpHostPort(Integer sftpHostPort) {
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
    @Column(name = "pgp_archive_public_key")
    public String getPgpArchivePublicKey() {
        return pgpArchivePublicKey;
    }

    public void setPgpArchivePublicKey(String pgpArchivePublicKey) {
        this.pgpArchivePublicKey = pgpArchivePublicKey;
    }

    @Basic
    @Column(name = "pgp_archive_private_key")
    public String getPgpArchivePrivateKey() {
        return pgpArchivePrivateKey;
    }

    public void setPgpArchivePrivateKey(String pgpArchivePrivateKey) {
        this.pgpArchivePrivateKey = pgpArchivePrivateKey;
    }
    */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractTechnicalDetailsEntity that = (ExtractTechnicalDetailsEntity) o;
        return Objects.equals(uuid, that.uuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sftpHostName, that.sftpHostName)
                /*
                &&
                Objects.equals(sftpHostPublicKey, that.sftpHostPublicKey) &&
                Objects.equals(sftpHostDirectory, that.sftpHostDirectory) &&
                Objects.equals(sftpHostPort, that.sftpHostPort) &&
                Objects.equals(sftpClientUsername, that.sftpClientUsername) &&
                Objects.equals(sftpClientPrivateKeyPassword, that.sftpClientPrivateKeyPassword) &&
                Objects.equals(sftpClientPrivateKey, that.sftpClientPrivateKey) &&
                Objects.equals(pgpCustomerPublicKey, that.pgpCustomerPublicKey) &&
                Objects.equals(pgpArchivePublicKey, that.pgpArchivePublicKey) &&
                Objects.equals(pgpArchivePrivateKey, that.pgpArchivePrivateKey)
                */
                ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, name, sftpHostName/*, sftpHostPublicKey, sftpHostDirectory, sftpHostPort,
                sftpClientUsername, sftpClientPrivateKeyPassword,sftpClientPrivateKey,
                pgpCustomerPublicKey, pgpArchivePublicKey, pgpArchivePrivateKey*/);
    }

}
