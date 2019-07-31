package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.ProjectApplicationPolicyEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;

public class SecurityProjectApplicationPolicyDAL {

    public ProjectApplicationPolicyEntity getProjectApplicationPolicyId(String projectUuid) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            ProjectApplicationPolicyEntity ret = entityManager.find(ProjectApplicationPolicyEntity.class, projectUuid);

            return ret;
        } finally {
            entityManager.close();
        }
    }
}
