package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.MasterMappingEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SecurityMasterMappingDAL {

    public List<String> getParentMappings(String childUuid, Short childMapTypeId, Short parentMapTypeId) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MasterMappingEntity> cq = cb.createQuery(MasterMappingEntity.class);
            Root<MasterMappingEntity> rootEntry = cq.from(MasterMappingEntity.class);

            Predicate predicate = cb.and(cb.equal(rootEntry.get("childUuid"), childUuid),
                    cb.equal(rootEntry.get("childMapTypeId"), childMapTypeId),
                    cb.equal(rootEntry.get("parentMapTypeId"), parentMapTypeId));

            cq.where(predicate);
            TypedQuery<MasterMappingEntity> query = entityManager.createQuery(cq);
            List<MasterMappingEntity> maps = query.getResultList();

            List<String> parents = new ArrayList<>();
            for (MasterMappingEntity mme : maps) {
                parents.add(mme.getParentUuid());
            }

            return parents;
        } finally {
            entityManager.close();
        }
    }

    public List<String> getChildMappings(String parentUuid, Short parentMapTypeId, Short childMapTypeId) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MasterMappingEntity> cq = cb.createQuery(MasterMappingEntity.class);
            Root<MasterMappingEntity> rootEntry = cq.from(MasterMappingEntity.class);

            Predicate predicate = cb.and(cb.equal(rootEntry.get("parentUuid"), parentUuid),
                    cb.equal(rootEntry.get("parentMapTypeId"), parentMapTypeId),
                    cb.equal(rootEntry.get("childMapTypeId"), childMapTypeId));

            cq.where(predicate);
            TypedQuery<MasterMappingEntity> query = entityManager.createQuery(cq);
            List<MasterMappingEntity> maps = query.getResultList();

            List<String> children = new ArrayList<>();
            for (MasterMappingEntity mme : maps) {
                children.add(mme.getChildUuid());
            }

            return children;
        } finally {
            entityManager.close();
        }

    }
}
