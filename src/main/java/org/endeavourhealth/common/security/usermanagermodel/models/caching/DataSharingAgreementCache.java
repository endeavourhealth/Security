package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityDataSharingAgreementDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DataSharingAgreementEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSharingAgreementCache {

    private static Map<String, DataSharingAgreementEntity> dataSharingAgreementMap = new ConcurrentHashMap<>();
    private static Map<String, List<DataSharingAgreementEntity>> allDSAsForAllChildRegion = new ConcurrentHashMap<>();
    private static Map<String, List<DataSharingAgreementEntity>> allDSAsForPublisher = new ConcurrentHashMap<>();
    private static Map<String, List<DataSharingAgreementEntity>> allDSAsForPublisherAndSubscriber = new ConcurrentHashMap<>();

    public static List<DataSharingAgreementEntity> getDSADetails(List<String> sharingAgreements) throws Exception {
        List<DataSharingAgreementEntity> dataSharingAgreementEntities = new ArrayList<>();
        List<String> missingDSAs = new ArrayList<>();

        for (String dsa : sharingAgreements) {
            DataSharingAgreementEntity dsaInMap = dataSharingAgreementMap.get(dsa);
            if (dsaInMap != null) {
                dataSharingAgreementEntities.add(dsaInMap);
            } else {
                missingDSAs.add(dsa);
            }
        }

        if (missingDSAs.size() > 0) {
            List<DataSharingAgreementEntity> entities = new SecurityDataSharingAgreementDAL().getDSAsFromList(missingDSAs);

            for (DataSharingAgreementEntity org : entities) {
                dataSharingAgreementMap.put(org.getUuid(), org);
                dataSharingAgreementEntities.add(org);
            }
        }

        CacheManager.startScheduler();

        return dataSharingAgreementEntities;

    }

    public static DataSharingAgreementEntity getDSADetails(String dsaId) throws Exception {

        DataSharingAgreementEntity dataSharingAgreementEntity = dataSharingAgreementMap.get(dsaId);
        if (dataSharingAgreementEntity == null) {
            dataSharingAgreementEntity = new SecurityDataSharingAgreementDAL().getDSA(dsaId);
            dataSharingAgreementMap.put(dataSharingAgreementEntity.getUuid(), dataSharingAgreementEntity);
        }

        CacheManager.startScheduler();

        return dataSharingAgreementEntity;

    }

    public static List<DataSharingAgreementEntity> getAllDSAsForAllChildRegions(String regionId) throws Exception {

        List <DataSharingAgreementEntity> allDSAs = allDSAsForAllChildRegion.get(regionId);
        if (allDSAs == null) {
            allDSAs = new SecurityDataSharingAgreementDAL().getAllDSAsForAllChildRegions(regionId);
            allDSAsForAllChildRegion.put(regionId, allDSAs);
        }

        CacheManager.startScheduler();

        return allDSAs;
    }

    public static List<DataSharingAgreementEntity> getAllDSAsForPublisherOrg(String odsCode) throws Exception {

        List <DataSharingAgreementEntity> allDSAs = allDSAsForPublisher.get(odsCode);
        if (allDSAs == null) {
            allDSAs = new SecurityDataSharingAgreementDAL().getAllDSAsForPublisherOrganisation(odsCode);
            allDSAsForPublisher.put(odsCode, allDSAs);
        }

        CacheManager.startScheduler();

        return allDSAs;
    }

    public static List<DataSharingAgreementEntity> getAllDSAsForPublisherAndSubscriber(String publisherOdsCode, String subscriberOdsCode) throws Exception {

        String key = publisherOdsCode + ":" + subscriberOdsCode;

        List <DataSharingAgreementEntity> allDSAs = allDSAsForPublisherAndSubscriber.get(key);
        if (allDSAs == null) {
            allDSAs = new SecurityDataSharingAgreementDAL().getDSAsWithMatchingPublisherAndSubscriber(publisherOdsCode, subscriberOdsCode);
            allDSAsForPublisherAndSubscriber.put(key, allDSAs);
        }

        CacheManager.startScheduler();

        return allDSAs;
    }

    public static void clearDataSharingAgreementCache(String dsaId) throws Exception {

        dataSharingAgreementMap.remove(dsaId);

        allDSAsForAllChildRegion.clear();
        allDSAsForPublisher.clear();
        allDSAsForPublisherAndSubscriber.clear();

    }

    public static void flushCache() throws Exception {
        dataSharingAgreementMap.clear();
        allDSAsForAllChildRegion.clear();
        allDSAsForPublisher.clear();
        allDSAsForPublisherAndSubscriber.clear();
    }
}
