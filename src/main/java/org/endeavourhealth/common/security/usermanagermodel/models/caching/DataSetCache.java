package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityDatasetDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DatasetEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSetCache {

    private static Map<String, DatasetEntity> dataSetMap = new ConcurrentHashMap<>();

    public static List<DatasetEntity> getDataSetDetails(List<String> dataSets) throws Exception {
        List<DatasetEntity> datasetEntities = new ArrayList<>();
        List<String> missingDataSets = new ArrayList<>();

        for (String ds : dataSets) {
            DatasetEntity dsInMap = dataSetMap.get(ds);
            if (dsInMap != null) {
                datasetEntities.add(dsInMap);
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

        DatasetEntity datasetEntity = dataSetMap.get(dataSetId);
        if (datasetEntity == null) {
            datasetEntity = new SecurityDatasetDAL().getDataSet(dataSetId);
            dataSetMap.put(datasetEntity.getUuid(), datasetEntity);
        }

        CacheManager.startScheduler();

        return datasetEntity;

    }

    public static void clearDataSetCache(String dataSetId) throws Exception {
        dataSetMap.remove(dataSetId);
    }

    public static void flushCache() throws Exception {
        dataSetMap.clear();
    }
}
