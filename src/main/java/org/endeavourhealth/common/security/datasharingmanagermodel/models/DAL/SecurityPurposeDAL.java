package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.PurposeEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SecurityPurposeDAL {

    // Duplicated from org.endeavourhealth.datasharingmanager.api.DAL.PurposeDAL - there must be a better way...

    public List<PurposeEntity> getPurposesFromList(List<String> purposes) throws Exception {
        if (purposes.isEmpty()) {
            return new ArrayList<PurposeEntity>();
        }

        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<PurposeEntity> cq = cb.createQuery(PurposeEntity.class);
            Root<PurposeEntity> rootEntry = cq.from(PurposeEntity.class);

            Predicate predicate = rootEntry.get("uuid").in(purposes);

            cq.where(predicate);
            TypedQuery<PurposeEntity> query = entityManager.createQuery(cq);

            List<PurposeEntity> ret = query.getResultList();

            ret.sort(Comparator.comparing(PurposeEntity::getTitle));

            return ret;
        } catch (Exception e) {
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
