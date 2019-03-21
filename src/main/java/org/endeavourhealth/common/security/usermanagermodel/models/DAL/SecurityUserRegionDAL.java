package org.endeavourhealth.common.security.usermanagermodel.models.DAL;

import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.database.UserRegionEntity;

import javax.persistence.EntityManager;

public class SecurityUserRegionDAL {

    public static UserRegionEntity getUserRegion(String userId) throws Exception {
        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        UserRegionEntity ret = entityManager.find(UserRegionEntity.class, userId);

        entityManager.close();

        return ret;
    }
}
