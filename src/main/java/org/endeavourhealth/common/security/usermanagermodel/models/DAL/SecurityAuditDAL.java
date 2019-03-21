package org.endeavourhealth.common.security.usermanagermodel.models.DAL;

import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.database.AuditEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.enums.AuditAction;
import org.endeavourhealth.common.security.usermanagermodel.models.enums.ItemType;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.UUID;

public class SecurityAuditDAL {

    public void addToAuditTrail(String userRoleId, AuditAction auditAction, ItemType itemType,
                                String itemBefore, String itemAfter, String auditJson) throws Exception {

        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        AuditEntity auditEntity = new AuditEntity();
        auditEntity.setId(UUID.randomUUID().toString());
        auditEntity.setTimestamp(new Timestamp(System.currentTimeMillis()));
        auditEntity.setAuditType(auditAction.getAuditAction().byteValue());
        auditEntity.setItemType(itemType.getItemType().byteValue());
        auditEntity.setUserProjectId(userRoleId);
        if (itemBefore != null) {
            auditEntity.setItemBefore(itemBefore);
        }
        if (itemAfter != null) {
            auditEntity.setItemAfter(itemAfter);
        }
        if (auditJson != null) {
            auditEntity.setAuditJson(auditJson);
        }


        entityManager.getTransaction().begin();
        entityManager.merge(auditEntity);
        entityManager.getTransaction().commit();

        entityManager.close();

    }
}
