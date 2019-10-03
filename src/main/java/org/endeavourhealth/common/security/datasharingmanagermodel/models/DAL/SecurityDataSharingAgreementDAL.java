package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DataSharingAgreementEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.enums.MapType;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.DataSharingAgreementCache;

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

        dsaUUIDs = getRegionDSAs(regionUUID, dsaUUIDs);
        List<DataSharingAgreementEntity> ret = new ArrayList<>();

        if (!dsaUUIDs.isEmpty())
            ret = DataSharingAgreementCache.getDSADetails(dsaUUIDs);

        return ret;

    }

    public List<String> getRegionDSAs(String regionUUID, List<String> dsaUUIDs) throws Exception {

        dsaUUIDs.addAll(new SecurityMasterMappingDAL().getChildMappings(regionUUID, MapType.REGION.getMapType(), MapType.DATASHARINGAGREEMENT.getMapType()));

        List<String> childRegions = new SecurityMasterMappingDAL().getChildMappings(regionUUID, MapType.REGION.getMapType(), MapType.REGION.getMapType());

        for (String region : childRegions) {
            dsaUUIDs = getRegionDSAs(region, dsaUUIDs);
        }

        return dsaUUIDs;
    }
}
