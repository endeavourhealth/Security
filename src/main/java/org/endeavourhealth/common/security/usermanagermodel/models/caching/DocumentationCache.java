package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityDocumentationDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.DocumentationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentationCache {

    private static Map<String, DocumentationEntity> documentationMap = new HashMap<>();

    public static List<DocumentationEntity> getDocumentDetails(List<String> documents) throws Exception {
        List<DocumentationEntity> documentationEntities = new ArrayList<>();
        List<String> missingDocuments = new ArrayList<>();

        for (String doc : documents) {
            if (documentationMap.containsKey(doc)) {
                documentationEntities.add(documentationMap.get(doc));
            } else {
                missingDocuments.add(doc);
            }
        }

        if (missingDocuments.size() > 0) {
            List<DocumentationEntity> entities = new SecurityDocumentationDAL().getDocumentsFromList(missingDocuments);

            for (DocumentationEntity doc : entities) {
                documentationMap.put(doc.getUuid(), doc);
                documentationEntities.add(doc);
            }
        }

        CacheManager.startScheduler();

        return documentationEntities;

    }

    public static DocumentationEntity getDocumentDetails(String documentId) throws Exception {
        DocumentationEntity documentationEntity = null;

        if (documentationMap.containsKey(documentId)) {
            documentationEntity = documentationMap.get(documentId);
        } else {
            documentationEntity = new SecurityDocumentationDAL().getDocument(documentId);
            documentationMap.put(documentationEntity.getUuid(), documentationEntity);
        }

        CacheManager.startScheduler();

        return documentationEntity;

    }

    public static void clearDocumentCache(String documentId) throws Exception {
        if (documentationMap.containsKey(documentId)) {
            documentationMap.remove(documentId);
        }
    }

    public static void flushCache() throws Exception {
        documentationMap.clear();
    }
}
