package org.endeavourhealth.common.security.usermanagermodel.models.DAL;

import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.database.UserApplicationPolicyEntity;

import javax.persistence.EntityManager;

public class SecurityUserApplicationPolicyDAL {

    public UserApplicationPolicyEntity getUserApplicationPolicy(String userId) throws Exception {
        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        UserApplicationPolicyEntity ret = entityManager.find(UserApplicationPolicyEntity.class, userId);

        entityManager.close();

        return ret;
    }
}
