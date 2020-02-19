package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityDataProcessingAgreementDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityOrganisationDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DataProcessingAgreementEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.OrganisationEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonOrganisationCCG;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OrganisationCache {

    private static Map<String, OrganisationEntity> organisationMap = new ConcurrentHashMap<>();
    private static Map<String, Boolean> organisationHasDPAMap = new ConcurrentHashMap<>();
    private static Map<String, String> organisationCCGMap = new ConcurrentHashMap<>();

    public static List<OrganisationEntity> getOrganisationDetails(List<String> organisations) throws Exception {
        List<OrganisationEntity> organisationEntities = new ArrayList<>();
        List<String> missingOrgs = new ArrayList<>();

        for (String org : organisations) {
            OrganisationEntity orgInMap = organisationMap.get(org);
            if (orgInMap != null) {
                organisationEntities.add(orgInMap);
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

        OrganisationEntity organisationEntity = organisationMap.get(organisationId);
        if (organisationEntity == null) {
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

    public static boolean doesOrganisationHaveDPA(String odsCode) throws Exception {

        Boolean orgHasDPA = organisationHasDPAMap.get(odsCode);
        if (orgHasDPA == null) {
            List<DataProcessingAgreementEntity> processingAgreementEntities = new SecurityDataProcessingAgreementDAL().getDataProcessingAgreementsForOrganisation(odsCode);
            orgHasDPA = new Boolean(processingAgreementEntities.size() > 0);
            organisationHasDPAMap.put(odsCode, orgHasDPA);
        }

        CacheManager.startScheduler();

        return orgHasDPA.booleanValue();

    }

    public static List<JsonOrganisationCCG> getCCGForOrganisationList(List<String> odsCodes) throws Exception {
        List<JsonOrganisationCCG> organisationCCGS = new ArrayList<>();
        List<String> missingOrgCCGs = new ArrayList<>();

        for (String ods : odsCodes) {
            String ccgInMap = organisationCCGMap.get(ods);
            if (ccgInMap != null) {
                JsonOrganisationCCG orgCCG = new JsonOrganisationCCG();
                orgCCG.setOdsCode(ods);
                orgCCG.setCcgName(ccgInMap);
                organisationCCGS.add(orgCCG);
            } else {
                missingOrgCCGs.add(ods);
            }
        }

        if (missingOrgCCGs.size() > 0) {
            List<JsonOrganisationCCG> orgCCGList = new SecurityOrganisationDAL().getCCGForOrganisationOdsList(missingOrgCCGs);

            for (JsonOrganisationCCG orgCCG : orgCCGList) {
                organisationCCGMap.put(orgCCG.getOdsCode(), orgCCG.getCcgName());
                organisationCCGS.add(orgCCG);
            }
        }

        CacheManager.startScheduler();

        return organisationCCGS;

    }

    public static void clearOrganisationCache(String organisationId) throws Exception {
        organisationMap.remove(organisationId);

        organisationHasDPAMap.remove(organisationId);
    }

    public static void flushCache() throws Exception {
        organisationMap.clear();
        organisationHasDPAMap.clear();
        organisationCCGMap.clear();
    }
}
