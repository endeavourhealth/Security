package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityCohortDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.CohortEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CohortCache {

    private static Map<String, CohortEntity> cohortMap = new ConcurrentHashMap<>();

    public static List<CohortEntity> getCohortDetails(List<String> cohorts) throws Exception {
        List<CohortEntity> cohortEntities = new ArrayList<>();
        List<String> missingCohorts = new ArrayList<>();

        for (String coh : cohorts) {
            CohortEntity cohortInMap = cohortMap.get(coh);
            if (cohortInMap != null) {
                cohortEntities.add(cohortInMap);
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

        CohortEntity cohortEntity = cohortMap.get(cohortId);
        if (cohortEntity == null) {
            cohortEntity = new SecurityCohortDAL().getCohort(cohortId);
            cohortMap.put(cohortEntity.getUuid(), cohortEntity);
        }

        CacheManager.startScheduler();

        return cohortEntity;

    }

    public static void clearCohortCache(String cohortId) throws Exception {
        cohortMap.remove(cohortId);
    }

    public static void flushCache() throws Exception {
        cohortMap.clear();
    }
}
