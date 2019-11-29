package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.CohortEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SecurityCohortDAL {

    public List<CohortEntity> getCohortsFromList(List<String> cohorts) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<CohortEntity> cq = cb.createQuery(CohortEntity.class);
            Root<CohortEntity> rootEntry = cq.from(CohortEntity.class);

            Predicate predicate = rootEntry.get("uuid").in(cohorts);

            cq.where(predicate);
            TypedQuery<CohortEntity> query = entityManager.createQuery(cq);

            List<CohortEntity> ret = query.getResultList();

            return ret;
        } finally {
            entityManager.close();
        }
    }

    public CohortEntity getCohort(String uuid) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {

            CohortEntity ret = entityManager.find(CohortEntity.class, uuid);

            return ret;
        } finally {
            entityManager.close();
        }
    }
}
