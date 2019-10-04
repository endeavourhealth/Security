package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.OrganisationEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.RegionEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.enums.MapType;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.OrganisationCache;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.RegionCache;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SecurityRegionDAL {

    public RegionEntity getSingleRegion(String uuid) throws Exception {

        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            RegionEntity ret = entityManager.find(RegionEntity.class, uuid);

            return ret;
        } finally {
            entityManager.close();
        }

    }

    public List<RegionEntity> getAllRegions() throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<RegionEntity> cq = cb.createQuery(RegionEntity.class);
            Root<RegionEntity> rootEntry = cq.from(RegionEntity.class);
            CriteriaQuery<RegionEntity> all = cq.select(rootEntry);
            TypedQuery<RegionEntity> allQuery = entityManager.createQuery(all);
            List<RegionEntity> ret = allQuery.getResultList();

            return ret;
        } finally {
            entityManager.close();
        }
    }

    public List<OrganisationEntity> getAllOrganisationsForAllChildRegions(String regionUUID) throws Exception {
        List<String> organisationUuids = new ArrayList<>();

        organisationUuids = getOrganisations(regionUUID, organisationUuids);
        List<OrganisationEntity> ret = new ArrayList<>();

        if (!organisationUuids.isEmpty())
            ret = OrganisationCache.getOrganisationDetails(organisationUuids);

        return ret;

    }

    public List<String> getOrganisations(String regionUUID, List<String> organisationUuids) throws Exception {

        organisationUuids.addAll(new SecurityMasterMappingDAL().getChildMappings(regionUUID, MapType.REGION.getMapType(), MapType.ORGANISATION.getMapType()));

        List<String> childRegions = new SecurityMasterMappingDAL().getChildMappings(regionUUID, MapType.REGION.getMapType(), MapType.REGION.getMapType());

        for (String region : childRegions) {
            organisationUuids = getOrganisations(region, organisationUuids);
        }

        return organisationUuids;
    }

    public List<RegionEntity> getAllChildRegionsForRegion(String regionId) throws Exception {
        List<String> regionUUIDs = new ArrayList<>();

        List<RegionEntity> ret = new ArrayList<>();

        regionUUIDs.add(regionId);

        regionUUIDs = getRegions(regionId, regionUUIDs);

        for (String regionUUID : regionUUIDs) {
            ret.add(RegionCache.getRegionDetails(regionUUID));
        }

        return ret;

    }

    public List<String> getRegions(String regionUUID, List<String> regionUUIDs) throws Exception {

        List<String> childRegions = new SecurityMasterMappingDAL().getChildMappings(regionUUID, MapType.REGION.getMapType(), MapType.REGION.getMapType());

        regionUUIDs.addAll(childRegions);

        for (String region : childRegions) {
            regionUUIDs = getRegions(region, regionUUIDs);
        }

        return regionUUIDs;
    }
}
