package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityRegionDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.OrganisationEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.RegionEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegionCache {
    private static Map<String, RegionEntity> regionMap = new ConcurrentHashMap<>();
    private static boolean allRegionsFound = false;
    private static Map<String, List<OrganisationEntity>> allOrgsForAllChildRegion = new ConcurrentHashMap<>();
    private static Map<String, List<RegionEntity>> allRegionsForUser = new ConcurrentHashMap<>();

    public static RegionEntity getRegionDetails(String regionId) throws Exception {

        RegionEntity foundRegion = regionMap.get(regionId);
        if (foundRegion == null) {
            foundRegion = new SecurityRegionDAL().getSingleRegion(regionId);
            regionMap.put(foundRegion.getUuid(), foundRegion);

        }

        CacheManager.startScheduler();

        return foundRegion;

    }

    public static List<RegionEntity> getRegionDetails(List<String> regions) throws Exception {
        List<RegionEntity> regionEntities = new ArrayList<>();
        List<String> missingRegions = new ArrayList<>();

        for (String reg : regions) {
            RegionEntity regInMap = regionMap.get(reg);
            if (regInMap != null) {
                regionEntities.add(regInMap);
            } else {
                missingRegions.add(reg);
            }
        }

        if (missingRegions.size() > 0) {
            List<RegionEntity> entities = new SecurityRegionDAL().getRegionsFromList(missingRegions);

            for (RegionEntity reg : entities) {
                regionMap.put(reg.getUuid(), reg);
                regionEntities.add(reg);
            }
        }

        CacheManager.startScheduler();

        return regionEntities;

    }

    public static List<RegionEntity> getAllRegions() throws Exception {

        if (!allRegionsFound) {
            List<RegionEntity> allRegions = new SecurityRegionDAL().getAllRegions();
            for (RegionEntity reg : allRegions) {
                regionMap.put(reg.getUuid(), reg);
            }
        }

        CacheManager.startScheduler();

        allRegionsFound = true;

        return new ArrayList(regionMap.values());

    }

    public static List<OrganisationEntity> getAllOrganisationsForAllChildRegions(String regionId) throws Exception {

        List<OrganisationEntity> allOrgs = allOrgsForAllChildRegion.get(regionId);
        if (allOrgs == null) {
            allOrgs = new SecurityRegionDAL().getAllOrganisationsForAllChildRegions(regionId);
            allOrgsForAllChildRegion.put(regionId, allOrgs);
        }

        CacheManager.startScheduler();

        return allOrgs;
    }

    public static List<RegionEntity> getAllChildRegionsForRegion(String regionId) throws Exception {

        List<RegionEntity> allRegions = allRegionsForUser.get(regionId);
        if (allRegions == null) {
            allRegions = new SecurityRegionDAL().getAllChildRegionsForRegion(regionId);
            allRegionsForUser.put(regionId, allRegions);
        }

        CacheManager.startScheduler();

        return allRegions;
    }

    public static void clearRegionCache(String regionId) throws Exception {
        regionMap.remove(regionId);

        allOrgsForAllChildRegion.clear();
        allRegionsForUser.clear();

        allRegionsFound = false;
    }

    public static void flushCache() throws Exception {
        regionMap.clear();
        allOrgsForAllChildRegion.clear();
        allRegionsFound = false;
        allRegionsForUser.clear();
    }
}
