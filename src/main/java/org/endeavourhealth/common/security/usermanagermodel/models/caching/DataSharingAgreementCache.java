package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityDataSharingAgreementDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DataSharingAgreementEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSharingAgreementCache {

    private static Map<String, DataSharingAgreementEntity> dataSharingAgreementMap = new HashMap<>();

    public static List<DataSharingAgreementEntity> getDSADetails(List<String> sharingAgreements) throws Exception {
        List<DataSharingAgreementEntity> dataSharingAgreementEntities = new ArrayList<>();
        List<String> missingDSAs = new ArrayList<>();

        for (String dsa : sharingAgreements) {
            if (dataSharingAgreementMap.containsKey(dsa)) {
                dataSharingAgreementEntities.add(dataSharingAgreementMap.get(dsa));
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
        DataSharingAgreementEntity dataSharingAgreementEntity = null;

        if (dataSharingAgreementMap.containsKey(dsaId)) {
            dataSharingAgreementEntity = dataSharingAgreementMap.get(dsaId);
        } else {
            dataSharingAgreementEntity = new SecurityDataSharingAgreementDAL().getDSA(dsaId);
            dataSharingAgreementMap.put(dataSharingAgreementEntity.getUuid(), dataSharingAgreementEntity);
        }

        CacheManager.startScheduler();

        return dataSharingAgreementEntity;

    }

    public static void flushCache() throws Exception {
        dataSharingAgreementMap.clear();
    }
}
