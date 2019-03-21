package org.endeavourhealth.common.security.usermanagermodel.models.DAL;

import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.database.ApplicationAccessProfileEntity;

import javax.persistence.EntityManager;

public class SecurityApplicationAccessProfileDAL {

    public ApplicationAccessProfileEntity getApplicationProfile(String applicationProfileId) throws Exception {
        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        ApplicationAccessProfileEntity ret = entityManager.find(ApplicationAccessProfileEntity.class, applicationProfileId);

        entityManager.close();

        return ret;
    }
}
