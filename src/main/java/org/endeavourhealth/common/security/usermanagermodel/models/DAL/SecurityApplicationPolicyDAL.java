package org.endeavourhealth.common.security.usermanagermodel.models.DAL;

import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.database.ApplicationPolicyEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SecurityApplicationPolicyDAL {

    public ApplicationPolicyEntity getApplicationPolicy(String roleId) throws Exception {
        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        try {
            ApplicationPolicyEntity ret = entityManager.find(ApplicationPolicyEntity.class, roleId);

            return ret;
        } finally {
            entityManager.close();
        }
    }

    public List<ApplicationPolicyEntity> getAllApplicationPolicies() throws Exception {
        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<ApplicationPolicyEntity> cq = cb.createQuery(ApplicationPolicyEntity.class);
            Root<ApplicationPolicyEntity> rootEntry = cq.from(ApplicationPolicyEntity.class);

            Predicate predicate = cb.equal(rootEntry.get("isDeleted"), 0);

            cq.where(predicate);

            TypedQuery<ApplicationPolicyEntity> query = entityManager.createQuery(cq);
            List<ApplicationPolicyEntity> ret = query.getResultList();

            return ret;
        } finally {
            entityManager.close();
        }
    }
}
