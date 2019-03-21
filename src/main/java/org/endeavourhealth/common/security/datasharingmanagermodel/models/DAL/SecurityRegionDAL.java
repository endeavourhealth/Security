package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.RegionEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class SecurityRegionDAL {

    public RegionEntity getSingleRegion(String uuid) throws Exception {

        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        RegionEntity ret = entityManager.find(RegionEntity.class, uuid);

        entityManager.close();

        return ret;

    }

    public List<RegionEntity> getAllRegions() throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RegionEntity> cq = cb.createQuery(RegionEntity.class);
        Root<RegionEntity> rootEntry = cq.from(RegionEntity.class);
        CriteriaQuery<RegionEntity> all = cq.select(rootEntry);
        TypedQuery<RegionEntity> allQuery = entityManager.createQuery(all);
        List<RegionEntity> ret = allQuery.getResultList();

        entityManager.close();

        return ret;
    }
}
