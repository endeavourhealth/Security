package org.endeavourhealth.common.security.usermanagermodel.models.DAL;

import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.database.DelegationEntity;

import javax.persistence.EntityManager;

public class SecurityDelegationDAL {

    public DelegationEntity getDelegation(String delegationId) throws Exception {
        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        DelegationEntity ret = entityManager.find(DelegationEntity.class, delegationId);

        entityManager.close();

        return ret;
    }
}