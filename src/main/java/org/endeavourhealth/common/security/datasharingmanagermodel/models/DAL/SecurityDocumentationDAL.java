package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DocumentationEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SecurityDocumentationDAL {

    public DocumentationEntity getDocument(String uuid) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            DocumentationEntity ret = entityManager.find(DocumentationEntity.class, uuid);

            return ret;
        } finally {
            entityManager.close();
        }
    }

    public List<DocumentationEntity> getDocumentsFromList(List<String> documents) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<DocumentationEntity> cq = cb.createQuery(DocumentationEntity.class);
            Root<DocumentationEntity> rootEntry = cq.from(DocumentationEntity.class);

            Predicate predicate = rootEntry.get("uuid").in(documents);

            cq.where(predicate);
            TypedQuery<DocumentationEntity> query = entityManager.createQuery(cq);

            List<DocumentationEntity> ret = query.getResultList();

            return ret;
        } finally {
            entityManager.close();
        }
    }
}
