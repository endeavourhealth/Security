package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityDatasetDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DatasetEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSetCache {

    private static Map<String, DatasetEntity> dataSetMap = new HashMap<>();

    public static List<DatasetEntity> getDataSetDetails(List<String> dataSets) throws Exception {
        List<DatasetEntity> datasetEntities = new ArrayList<>();
        List<String> missingDataSets = new ArrayList<>();

        for (String ds : dataSets) {
            if (dataSetMap.containsKey(ds)) {
                datasetEntities.add(dataSetMap.get(ds));
            } else {
                missingDataSets.add(ds);
            }
        }

        if (missingDataSets.size() > 0) {
            List<DatasetEntity> entities = new SecurityDatasetDAL().getDataSetsFromList(missingDataSets);

            for (DatasetEntity ds : entities) {
                dataSetMap.put(ds.getUuid(), ds);
                datasetEntities.add(ds);
            }
        }

        CacheManager.startScheduler();

        return datasetEntities;

    }

    public static DatasetEntity getDataSetDetails(String dataSetId) throws Exception {
        DatasetEntity datasetEntity = null;

        if (dataSetMap.containsKey(dataSetId)) {
            datasetEntity = dataSetMap.get(dataSetId);
        } else {
            datasetEntity = new SecurityDatasetDAL().getDataSet(dataSetId);
            dataSetMap.put(datasetEntity.getUuid(), datasetEntity);
        }

        CacheManager.startScheduler();

        return datasetEntity;

    }

    public static void clearDataSetCache(String dataSetId) throws Exception {
        if (dataSetMap.containsKey(dataSetId)) {
            dataSetMap.remove(dataSetId);
        }
    }

    public static void flushCache() throws Exception {
        dataSetMap.clear();
    }
}
