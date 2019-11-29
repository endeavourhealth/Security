package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityCohortDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.CohortEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CohortCache {

    private static Map<String, CohortEntity> cohortMap = new HashMap<>();

    public static List<CohortEntity> getCohortDetails(List<String> cohorts) throws Exception {
        List<CohortEntity> cohortEntities = new ArrayList<>();
        List<String> missingCohorts = new ArrayList<>();

        for (String coh : cohorts) {
            if (cohortMap.containsKey(coh)) {
                cohortEntities.add(cohortMap.get(coh));
            } else {
                missingCohorts.add(coh);
            }
        }

        if (missingCohorts.size() > 0) {
            List<CohortEntity> entities = new SecurityCohortDAL().getCohortsFromList(missingCohorts);

            for (CohortEntity ds : entities) {
                cohortMap.put(ds.getUuid(), ds);
                cohortEntities.add(ds);
            }
        }

        CacheManager.startScheduler();

        return cohortEntities;

    }

    public static CohortEntity getCohortDetails(String cohortId) throws Exception {
        CohortEntity datasetEntity = null;

        if (cohortMap.containsKey(cohortId)) {
            datasetEntity = cohortMap.get(cohortId);
        } else {
            datasetEntity = new SecurityCohortDAL().getCohort(cohortId);
            cohortMap.put(datasetEntity.getUuid(), datasetEntity);
        }

        CacheManager.startScheduler();

        return datasetEntity;

    }

    public static void clearCohortCache(String cohortId) throws Exception {
        if (cohortMap.containsKey(cohortId)) {
            cohortMap.remove(cohortId);
        }
    }

    public static void flushCache() throws Exception {
        cohortMap.clear();
    }
}
