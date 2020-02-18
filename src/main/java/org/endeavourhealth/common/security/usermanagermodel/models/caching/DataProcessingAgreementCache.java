package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityDataProcessingAgreementDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DataProcessingAgreementEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataProcessingAgreementCache {

    private static Map<String, DataProcessingAgreementEntity> dataProcessingAgreementMap = new ConcurrentHashMap<>();
    private static Map<String, List<DataProcessingAgreementEntity>> allDPAsForAllChildRegion = new ConcurrentHashMap<>();

    public static List<DataProcessingAgreementEntity> getDPADetails(List<String> processingAgreements) throws Exception {
        List<DataProcessingAgreementEntity> dataProcessingAgreementEntities = new ArrayList<>();
        List<String> missingDSAs = new ArrayList<>();

        for (String dsa : processingAgreements) {
            DataProcessingAgreementEntity dpaInMap = dataProcessingAgreementMap.get(dsa);
            if (dpaInMap != null) {
                dataProcessingAgreementEntities.add(dpaInMap);
            } else {
                missingDSAs.add(dsa);
            }
        }

        if (missingDSAs.size() > 0) {
            List<DataProcessingAgreementEntity> entities = new SecurityDataProcessingAgreementDAL().getDPAsFromList(missingDSAs);

            for (DataProcessingAgreementEntity org : entities) {
                dataProcessingAgreementMap.put(org.getUuid(), org);
                dataProcessingAgreementEntities.add(org);
            }
        }

        CacheManager.startScheduler();

        return dataProcessingAgreementEntities;

    }

    public static DataProcessingAgreementEntity getDPADetails(String dsaId) throws Exception {

        DataProcessingAgreementEntity dataProcessingAgreementEntity = dataProcessingAgreementMap.get(dsaId);
        if (dataProcessingAgreementEntity == null) {
            dataProcessingAgreementEntity = new SecurityDataProcessingAgreementDAL().getDPA(dsaId);
            dataProcessingAgreementMap.put(dataProcessingAgreementEntity.getUuid(), dataProcessingAgreementEntity);
        }

        CacheManager.startScheduler();

        return dataProcessingAgreementEntity;

    }

    public static List<DataProcessingAgreementEntity> getAllDPAsForAllChildRegions(String regionId) throws Exception {

        List <DataProcessingAgreementEntity> allDPAs = allDPAsForAllChildRegion.get(regionId);
        if (allDPAs == null) {
            allDPAs = new SecurityDataProcessingAgreementDAL().getAllDPAsForAllChildRegions(regionId);
            allDPAsForAllChildRegion.put(regionId, allDPAs);
        }

        CacheManager.startScheduler();

        return allDPAs;
    }

    public static void clearDataProcessingAgreementCache(String dpaId) throws Exception {
        dataProcessingAgreementMap.remove(dpaId);

        allDPAsForAllChildRegion.clear();

    }

    public static void flushCache() throws Exception {
        dataProcessingAgreementMap.clear();
        allDPAsForAllChildRegion.clear();
    }
}
