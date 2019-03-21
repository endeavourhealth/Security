package org.endeavourhealth.common.security.usermanagermodel.models.DAL;

import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.database.ApplicationEntity;

import javax.persistence.EntityManager;

public class SecurityApplicationDAL {
    public ApplicationEntity getApplication(String applicationId) throws Exception {
        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        ApplicationEntity ret = entityManager.find(ApplicationEntity.class, applicationId);

        entityManager.close();

        return ret;
    }
}
