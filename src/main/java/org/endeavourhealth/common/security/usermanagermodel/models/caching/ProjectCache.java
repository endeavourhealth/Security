package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityProjectApplicationPolicyDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityProjectDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.ProjectApplicationPolicyEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.ProjectEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectCache {

    private static Map<String, ProjectEntity> projectMap = new ConcurrentHashMap<>();
    private static Map<String, JsonProject> jsonProjectMap = new ConcurrentHashMap<>();
    private static Map<String, String> projectApplicationPolicyMap = new ConcurrentHashMap<>();
    private static Map<String, List<ProjectEntity>> allProjectsForAllChildRegion = new ConcurrentHashMap<>();
    private static Map<String, List<String>> allPublishersForProjectWithSubCheck = new ConcurrentHashMap<>();

    public static List<ProjectEntity> getProjectDetails(List<String> projects) throws Exception {
        List<ProjectEntity> projectEntities = new ArrayList<>();
        List<String> missingProjects = new ArrayList<>();

        for (String org : projects) {
            ProjectEntity projInMap = projectMap.get(org);
            if (projInMap != null) {
                projectEntities.add(projInMap);
            } else {
                missingProjects.add(org);
            }
        }

        if (missingProjects.size() > 0) {
            List<ProjectEntity> entities = new SecurityProjectDAL().getProjectsFromList(missingProjects);

            for (ProjectEntity org : entities) {
                projectMap.put(org.getUuid(), org);
                projectEntities.add(org);
            }
        }

        CacheManager.startScheduler();

        return projectEntities;

    }

    public static ProjectEntity getProjectDetails(String projectId) throws Exception {

        ProjectEntity projectEntity = projectMap.get(projectId);
        if (projectEntity == null) {
            projectEntity = new SecurityProjectDAL().getProject(projectId);
            projectMap.put(projectEntity.getUuid(), projectEntity);
        }

        CacheManager.startScheduler();

        return projectEntity;

    }

    public static JsonProject getJsonProjectDetails(String projectId) throws Exception {

        JsonProject project = jsonProjectMap.get(projectId);
        if (project == null) {
            project = new SecurityProjectDAL().getFullProjectJson(projectId);
            jsonProjectMap.put(project.getUuid(), project);
        }

        CacheManager.startScheduler();

        return project;

    }

    public static String getProjectApplicationPolicy(String projectId) throws Exception {

        String foundPolicy = projectApplicationPolicyMap.get(projectId);
        if (foundPolicy == null) {
            ProjectApplicationPolicyEntity policyApp = new SecurityProjectApplicationPolicyDAL().getProjectApplicationPolicyId(projectId);
            foundPolicy = policyApp.getApplicationPolicyId();
            projectApplicationPolicyMap.put(projectId, foundPolicy);
        }

        CacheManager.startScheduler();

        return foundPolicy;
    }

    public static List<ProjectEntity> getAllProjectsForAllChildRegions(String regionId) throws Exception {

        List<ProjectEntity> allProjects = allProjectsForAllChildRegion.get(regionId);
        if (allProjects == null) {
            allProjects = new SecurityProjectDAL().getProjectsForRegion(regionId);
            allProjectsForAllChildRegion.put(regionId, allProjects);
        }

        CacheManager.startScheduler();

        return allProjects;
    }

    public static List<String> getAllPublishersForProjectWithSubscriberCheck(String projectId, String requesterOdsCode) throws Exception {
        String key = projectId + ":" + requesterOdsCode;

        List<String> pubOdsCodes = allPublishersForProjectWithSubCheck.get(key);
        if (pubOdsCodes == null) {
            pubOdsCodes = new SecurityProjectDAL().getPublishersForProject(projectId, requesterOdsCode);
            allPublishersForProjectWithSubCheck.put(key, pubOdsCodes);
        }

        CacheManager.startScheduler();

        return pubOdsCodes;
    }

    public static void clearProjectCache(String projectId) throws Exception {
        projectMap.remove(projectId);

        jsonProjectMap.remove(projectId);

        projectApplicationPolicyMap.remove(projectId);

        allProjectsForAllChildRegion.clear();
        allPublishersForProjectWithSubCheck.clear();
    }

    public static void flushCache() throws Exception {
        projectMap.clear();
        jsonProjectMap.clear();
        projectApplicationPolicyMap.clear();
        allProjectsForAllChildRegion.clear();
    }
}
