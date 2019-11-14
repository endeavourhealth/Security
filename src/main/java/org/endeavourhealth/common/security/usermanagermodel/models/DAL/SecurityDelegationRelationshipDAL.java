package org.endeavourhealth.common.security.usermanagermodel.models.DAL;

import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.database.DelegationRelationshipEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SecurityDelegationRelationshipDAL {


    public List<DelegationRelationshipEntity> getDelegatedOrganisations(String organisationId) throws Exception {
        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<DelegationRelationshipEntity> cq = cb.createQuery(DelegationRelationshipEntity.class);
            Root<DelegationRelationshipEntity> rootEntry = cq.from(DelegationRelationshipEntity.class);

            Predicate predicate = cb.and(cb.equal(rootEntry.get("parentUuid"), organisationId),
                    (cb.equal(rootEntry.get("isDeleted"), 0)));

            cq.where(predicate);
            TypedQuery<DelegationRelationshipEntity> query = entityManager.createQuery(cq);
            List<DelegationRelationshipEntity> ret = query.getResultList();

            return ret;
        } finally {
            entityManager.close();
        }
    }

    public DelegationRelationshipEntity getDelegationRelationship(String relationshipId) throws Exception {
        EntityManager entityManager = ConnectionManager.getUmEntityManager();

        try {
            DelegationRelationshipEntity ret = entityManager.find(DelegationRelationshipEntity.class, relationshipId);

            return ret;

        } finally {
            entityManager.close();
        }
    }
}
