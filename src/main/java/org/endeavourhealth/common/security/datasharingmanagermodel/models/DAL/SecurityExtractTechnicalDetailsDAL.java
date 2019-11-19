package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.ExtractTechnicalDetailsEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;

public class SecurityExtractTechnicalDetailsDAL {

    public ExtractTechnicalDetailsEntity getExtractTechnicalDetails(String uuid) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            ExtractTechnicalDetailsEntity ret = entityManager.find(ExtractTechnicalDetailsEntity.class, uuid);

            return ret;
        } finally {
            entityManager.close();
        }
    }

}
