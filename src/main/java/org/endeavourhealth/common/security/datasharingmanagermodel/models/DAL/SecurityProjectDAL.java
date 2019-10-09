package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.*;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.enums.MapType;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonAuthorityToShare;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonProject;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityUserProjectDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.DataSharingAgreementCache;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.OrganisationCache;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.ProjectCache;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.UserCache;
import org.endeavourhealth.common.security.usermanagermodel.models.database.UserProjectEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.json.JsonUser;
import org.keycloak.representations.idm.UserRepresentation;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

public class SecurityProjectDAL {

    public List<ProjectEntity> getProjectsFromList(List<String> projects) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<ProjectEntity> cq = cb.createQuery(ProjectEntity.class);
            Root<ProjectEntity> rootEntry = cq.from(ProjectEntity.class);

            Predicate predicate = rootEntry.get("uuid").in(projects);

            cq.where(predicate);
            TypedQuery<ProjectEntity> query = entityManager.createQuery(cq);

            List<ProjectEntity> ret = query.getResultList();

            return ret;
        } finally {
            entityManager.close();
        }
    }

    public ProjectEntity getProject(String uuid) throws Exception {
        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            ProjectEntity ret = entityManager.find(ProjectEntity.class, uuid);

            return ret;
        } finally {
            entityManager.close();
        }
    }

    public List<JsonAuthorityToShare> getUsersAssignedToProject(String projectUuid) throws Exception {

        List<UserProjectEntity> userProjects = new SecurityUserProjectDAL().getUserProjectEntitiesForProject(projectUuid);

        List<JsonAuthorityToShare> authorities = new ArrayList<>();

        for (UserProjectEntity userProject : userProjects) {
            JsonAuthorityToShare auth = authorities.stream().filter(a -> a.getOrganisationId().equals(userProject.getOrganisationId())).findFirst().orElse(new JsonAuthorityToShare());
            if (auth.getOrganisationId() == null) {
                OrganisationEntity org = OrganisationCache.getOrganisationDetails(userProject.getOrganisationId());
                auth.setOrganisationId(org.getUuid());
                auth.setOrganisationName(org.getName());
                auth.setOrganisationOdsCode(org.getOdsCode());

                authorities.add(auth);
            }
            UserRepresentation u = UserCache.getUserDetails(userProject.getUserId());
            if (u != null){
                JsonUser jsonUser = new JsonUser(u);
                auth.addUser(jsonUser);
            }

        }
        return authorities;
    }
    public JsonProject getFullProjectJson(String projectId) throws Exception {
        JsonProject project = new JsonProject(getProject(projectId));

        List<DataSharingAgreementEntity> dsas = getLinkedDsas(projectId);
        List<CohortEntity> basePopulations = getBasePopulations(projectId);
        List<DatasetEntity> dataSets = getDataSets(projectId);
        List<OrganisationEntity> publishers = getLinkedOrganisations(projectId, MapType.PUBLISHER.getMapType());
        List<OrganisationEntity> subscribers = getLinkedOrganisations(projectId, MapType.SUBSCRIBER.getMapType());
        ProjectApplicationPolicyEntity applicationPolicy = new SecurityProjectApplicationPolicyDAL().getProjectApplicationPolicyId(projectId);

        if (dsas != null) {
            Map<UUID, String> sharingAgreements = new HashMap<>();

            for (DataSharingAgreementEntity dsa : dsas) {
                sharingAgreements.put(UUID.fromString(dsa.getUuid()), dsa.getName());
            }
            project.setDsas(sharingAgreements);
        }

        if (basePopulations != null) {
            Map<UUID, String> populations = new HashMap<>();

            for (CohortEntity pop : basePopulations) {
                populations.put(UUID.fromString(pop.getUuid()), pop.getName());
            }
            project.setBasePopulation(populations);
        }

        if (dataSets != null) {
            Map<UUID, String> data = new HashMap<>();

            for (DatasetEntity ds : dataSets) {
                data.put(UUID.fromString(ds.getUuid()), ds.getName());
            }
            project.setDataSet(data);
        }

        if (publishers != null) {
            Map<UUID, String> pubs = new HashMap<>();

            for (OrganisationEntity pub : publishers) {
                pubs.put(UUID.fromString(pub.getUuid()), pub.getName());
            }
            project.setPublishers(pubs);
        }

        if (subscribers != null) {
            Map<UUID, String> subs = new HashMap<>();

            for (OrganisationEntity sub : subscribers) {
                subs.put(UUID.fromString(sub.getUuid()), sub.getName());
            }
            project.setSubscribers(subs);
        }

        if (applicationPolicy != null) {
            project.setApplicationPolicy(applicationPolicy.getApplicationPolicyId());
        }

        return project;
    }

    public List<DataSharingAgreementEntity> getLinkedDsas(String projectId) throws Exception {

        List<String> dsaUuids = new SecurityMasterMappingDAL().getParentMappings(projectId, MapType.PROJECT.getMapType(), MapType.DATASHARINGAGREEMENT.getMapType());
        List<DataSharingAgreementEntity> ret = new ArrayList<>();

        if (!dsaUuids.isEmpty())
            ret = DataSharingAgreementCache.getDSADetails(dsaUuids);

        return ret;
    }

    public List<CohortEntity> getBasePopulations(String projectId) throws Exception {

        List<String> cohortIds = new SecurityMasterMappingDAL().getChildMappings(projectId, MapType.PROJECT.getMapType(), MapType.COHORT.getMapType());
        List<CohortEntity> ret = new ArrayList<>();

        if (!cohortIds.isEmpty())
            ret = new SecurityCohortDAL().getCohortsFromList(cohortIds);

        return ret;
    }

    public List<DatasetEntity> getDataSets(String projectId) throws Exception {

        List<String> dataSetIds = new SecurityMasterMappingDAL().getChildMappings(projectId, MapType.PROJECT.getMapType(), MapType.DATASET.getMapType());
        List<DatasetEntity> ret = new ArrayList<>();

        if (!dataSetIds.isEmpty())
            ret = new SecurityDatasetDAL().getDataSetsFromList(dataSetIds);

        return ret;
    }

    public List<OrganisationEntity> getLinkedOrganisations(String projectId, Short mapType) throws Exception {

        List<String> orgUUIDs = new SecurityMasterMappingDAL().getChildMappings(projectId, MapType.PROJECT.getMapType(), mapType);
        List<OrganisationEntity> ret = new ArrayList<>();

        if (!orgUUIDs.isEmpty())
            ret = OrganisationCache.getOrganisationDetails(orgUUIDs);

        return ret;
    }

    public List<ProjectEntity> getProjectsForOrganisation(String organisationId) throws Exception {

        EntityManager entityManager = ConnectionManager.getDsmEntityManager();

        try {
            Query query = entityManager.createQuery(
                    "select p from ProjectEntity p " +
                            "inner join MasterMappingEntity mm on mm.parentUuid = p.uuid and mm.parentMapTypeId = :projectType " +
                            "inner join OrganisationEntity o on o.uuid = mm.childUuid " +
                            "where o.uuid = :orgUuid " +
                            "and mm.childMapTypeId = :subscriberType ");
            query.setParameter("projectType", MapType.PROJECT.getMapType());
            query.setParameter("orgUuid", organisationId);
            query.setParameter("subscriberType", MapType.SUBSCRIBER.getMapType());

            List<ProjectEntity> result = query.getResultList();

            return result;
        } finally {
            entityManager.close();
        }
    }

    public List<ProjectEntity> getProjectsForRegion(String regionUUID) throws Exception {

        List<DataSharingAgreementEntity> dsaUUIDs = DataSharingAgreementCache.getAllDSAsForAllChildRegions(regionUUID);

        List<String> projectUUIDs = new ArrayList<>();

        for (DataSharingAgreementEntity dsa : dsaUUIDs) {
            projectUUIDs.addAll(new SecurityMasterMappingDAL().getChildMappings(dsa.getUuid(), MapType.DATASHARINGAGREEMENT.getMapType(), MapType.PROJECT.getMapType()));
        }

        List<ProjectEntity> ret = new ArrayList<>();

        if (!projectUUIDs.isEmpty())
            ret = ProjectCache.getProjectDetails(projectUUIDs);

        return ret;
    }

}
