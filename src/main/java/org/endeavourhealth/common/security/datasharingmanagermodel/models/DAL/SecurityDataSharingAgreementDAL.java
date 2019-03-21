package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DataSharingAgreementEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SecurityDataSharingAgreementDAL {

    public DataSharingAgreementEntity getDSA(String uuid) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        DataSharingAgreementEntity ret = entityManager.find(DataSharingAgreementEntity.class, uuid);

        entityManager.close();

        return ret;
    }

    public List<DataSharingAgreementEntity> getDSAsFromList(List<String> dsas) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DataSharingAgreementEntity> cq = cb.createQuery(DataSharingAgreementEntity.class);
        Root<DataSharingAgreementEntity> rootEntry = cq.from(DataSharingAgreementEntity.class);

        Predicate predicate = rootEntry.get("uuid").in(dsas);

        cq.where(predicate);
        TypedQuery<DataSharingAgreementEntity> query = entityManager.createQuery(cq);

        List<DataSharingAgreementEntity> ret = query.getResultList();

        entityManager.close();

        return ret;
    }
}
