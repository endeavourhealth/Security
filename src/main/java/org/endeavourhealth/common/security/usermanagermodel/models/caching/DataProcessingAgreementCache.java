package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityDataProcessingAgreementDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DataProcessingAgreementEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProcessingAgreementCache {

    private static Map<String, DataProcessingAgreementEntity> dataProcessingAgreementMap = new HashMap<>();

    public static List<DataProcessingAgreementEntity> getDPADetails(List<String> processingAgreements) throws Exception {
        List<DataProcessingAgreementEntity> dataProcessingAgreementEntities = new ArrayList<>();
        List<String> missingDSAs = new ArrayList<>();

        for (String dsa : processingAgreements) {
            if (dataProcessingAgreementMap.containsKey(dsa)) {
                dataProcessingAgreementEntities.add(dataProcessingAgreementMap.get(dsa));
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
        DataProcessingAgreementEntity dataProcessingAgreementEntity = null;

        if (dataProcessingAgreementMap.containsKey(dsaId)) {
            dataProcessingAgreementEntity = dataProcessingAgreementMap.get(dsaId);
        } else {
            dataProcessingAgreementEntity = new SecurityDataProcessingAgreementDAL().getDPA(dsaId);
            dataProcessingAgreementMap.put(dataProcessingAgreementEntity.getUuid(), dataProcessingAgreementEntity);
        }

        CacheManager.startScheduler();

        return dataProcessingAgreementEntity;

    }

    public static void clearDataProcessingAgreementCache(String dpaId) throws Exception {
        if (dataProcessingAgreementMap.containsKey(dpaId)) {
            dataProcessingAgreementMap.remove(dpaId);
        }

    }

    public static void flushCache() throws Exception {
        dataProcessingAgreementMap.clear();
    }
}
