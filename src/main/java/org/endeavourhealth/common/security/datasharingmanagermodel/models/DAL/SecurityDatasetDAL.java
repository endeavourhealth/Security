package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DatasetEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SecurityDatasetDAL {

    public List<DatasetEntity> getDataSetsFromList(List<String> datasets) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DatasetEntity> cq = cb.createQuery(DatasetEntity.class);
        Root<DatasetEntity> rootEntry = cq.from(DatasetEntity.class);

        Predicate predicate = rootEntry.get("uuid").in(datasets);

        cq.where(predicate);
        TypedQuery<DatasetEntity> query = entityManager.createQuery(cq);

        List<DatasetEntity> ret = query.getResultList();

        entityManager.close();

        return ret;
    }
}
