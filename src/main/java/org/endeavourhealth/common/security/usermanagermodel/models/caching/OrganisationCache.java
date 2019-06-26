package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityOrganisationDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.OrganisationEntity;

import java.util.*;

public class OrganisationCache {

    private static Map<String, OrganisationEntity> organisationMap = new HashMap<>();

    public static List<OrganisationEntity> getOrganisationDetails(List<String> organisations) throws Exception {
        List<OrganisationEntity> organisationEntities = new ArrayList<>();
        List<String> missingOrgs = new ArrayList<>();

        for (String org : organisations) {
            if (organisationMap.containsKey(org)) {
                organisationEntities.add(organisationMap.get(org));
            } else {
                missingOrgs.add(org);
            }
        }

        if (missingOrgs.size() > 0) {
            List<OrganisationEntity> entities = new SecurityOrganisationDAL().getOrganisationsFromList(missingOrgs);

            for (OrganisationEntity org : entities) {
                organisationMap.put(org.getUuid(), org);
                organisationEntities.add(org);
            }
        }

        CacheManager.startScheduler();

        return organisationEntities;

    }

    public static OrganisationEntity getOrganisationDetails(String organisationId) throws Exception {
        OrganisationEntity organisationEntity = null;

        if (organisationMap.containsKey(organisationId)) {
            organisationEntity = organisationMap.get(organisationId);
        } else {
            organisationEntity = new SecurityOrganisationDAL().getOrganisation(organisationId);
            organisationMap.put(organisationEntity.getUuid(), organisationEntity);
        }

        CacheManager.startScheduler();

        return organisationEntity;

    }

    public static OrganisationEntity getOrganisationDetailsFromOdsCode(String odsCode) throws Exception {

        OrganisationEntity foundOrg = findOrgByOdsInCache(odsCode);
        if (foundOrg == null) {
            foundOrg = new SecurityOrganisationDAL().getOrganisationsFromOdsCode(odsCode);
            organisationMap.put(foundOrg.getUuid(), foundOrg);
        }

        CacheManager.startScheduler();

        return foundOrg;

    }

    public static List<OrganisationEntity> getOrganisationDetailsFromOdsCodeList(List<String> odsCodes) throws Exception {

        List<OrganisationEntity> organisationEntities = new ArrayList<>();
        List<String> missingOrgs = new ArrayList<>();

        for (String odsCode : odsCodes) {
            OrganisationEntity foundOrg = findOrgByOdsInCache(odsCode);
            if (foundOrg == null) {
                missingOrgs.add(odsCode);
            } else {
                organisationEntities.add(foundOrg);
            }
        }

        if (missingOrgs.size() > 0) {
            List<OrganisationEntity> entities = new SecurityOrganisationDAL().getOrganisationsFromOdsList(missingOrgs);

            for (OrganisationEntity org : entities) {
                organisationMap.put(org.getUuid(), org);
                organisationEntities.add(org);
            }
        }
        CacheManager.startScheduler();

        return organisationEntities;

    }

    private static OrganisationEntity findOrgByOdsInCache(String odsCode) throws Exception {

        for (OrganisationEntity org : organisationMap.values()) {
            if (org.getOdsCode().toLowerCase().equals(odsCode.trim().toLowerCase())) {
                return org;
            }
        }

        return null;
    }

    public static void flushCache() throws Exception {
        organisationMap.clear();
    }
}
