package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DataSharingAgreementEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.RegionEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.enums.MapType;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.DataSharingAgreementCache;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.RegionCache;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SecurityDataSharingAgreementDAL {

    public DataSharingAgreementEntity getDSA(String uuid) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            DataSharingAgreementEntity ret = entityManager.find(DataSharingAgreementEntity.class, uuid);

            return ret;
        } finally {
            entityManager.close();
        }
    }

    public List<DataSharingAgreementEntity> getDSAsFromList(List<String> dsas) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<DataSharingAgreementEntity> cq = cb.createQuery(DataSharingAgreementEntity.class);
            Root<DataSharingAgreementEntity> rootEntry = cq.from(DataSharingAgreementEntity.class);

            Predicate predicate = rootEntry.get("uuid").in(dsas);

            cq.where(predicate);
            TypedQuery<DataSharingAgreementEntity> query = entityManager.createQuery(cq);

            List<DataSharingAgreementEntity> ret = query.getResultList();

            return ret;
        } finally {
            entityManager.close();
        }
    }

    public List<DataSharingAgreementEntity> getAllDSAsForAllChildRegions(String regionUUID) throws Exception {
        List<String> dsaUUIDs = new ArrayList<>();

        List<RegionEntity> allRegions = RegionCache.getAllChildRegionsForRegion(regionUUID);

        for (RegionEntity region : allRegions) {
            dsaUUIDs.addAll(new SecurityMasterMappingDAL().getChildMappings(region.getUuid(), MapType.REGION.getMapType(), MapType.DATASHARINGAGREEMENT.getMapType()));
        }

        List<DataSharingAgreementEntity> ret = new ArrayList<>();

        if (!dsaUUIDs.isEmpty())
            ret = DataSharingAgreementCache.getDSADetails(dsaUUIDs);

        return ret;

    }
}
