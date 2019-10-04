package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityRegionDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.OrganisationEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.RegionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionCache {
    private static Map<String, RegionEntity> regionMap = new HashMap<>();
    private static boolean allRegionsFound = false;
    private static Map<String, List<OrganisationEntity>> allOrgsForAllChildRegion = new HashMap<>();
    private static Map<String, List<RegionEntity>> allRegionsForUser = new HashMap<>();

    public static RegionEntity getRegionDetails(String regionId) throws Exception {
        RegionEntity foundRegion = null;

        if (regionMap.containsKey(regionId)) {
            foundRegion = regionMap.get(regionId);
        } else {
            foundRegion = new SecurityRegionDAL().getSingleRegion(regionId);
            regionMap.put(foundRegion.getUuid(), foundRegion);

        }

        CacheManager.startScheduler();

        return foundRegion;

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
        List <OrganisationEntity> allOrgs = new ArrayList<>();

        if (allOrgsForAllChildRegion.containsKey(regionId)) {
            allOrgs = allOrgsForAllChildRegion.get(regionId);
        } else {
            allOrgs = new SecurityRegionDAL().getAllOrganisationsForAllChildRegions(regionId);
            allOrgsForAllChildRegion.put(regionId, allOrgs);
        }

        CacheManager.startScheduler();

        return allOrgs;
    }

    public static List<RegionEntity> getAllChildRegionsForRegion(String regionId) throws Exception {
        List <RegionEntity> allRegions = new ArrayList<>();

        if (allRegionsForUser.containsKey(regionId)) {
            allRegions = allRegionsForUser.get(regionId);
        } else {
            allRegions = new SecurityRegionDAL().getAllChildRegionsForRegion(regionId);
            allRegionsForUser.put(regionId, allRegions);
        }

        CacheManager.startScheduler();

        return allRegions;
    }

    public static void clearRegionCache(String regionId) throws Exception {
        if (regionMap.containsKey(regionId)) {
            regionMap.remove(regionId);
        }

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
