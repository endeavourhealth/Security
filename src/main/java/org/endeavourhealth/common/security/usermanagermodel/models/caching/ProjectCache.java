package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityProjectApplicationPolicyDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL.SecurityProjectDAL;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.ProjectApplicationPolicyEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.ProjectEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectCache {

    private static Map<String, ProjectEntity> projectMap = new HashMap<>();
    private static Map<String, JsonProject> jsonProjectMap = new HashMap<>();
    private static Map<String, String> projectApplicationPolicyMap = new HashMap<>();
    private static Map<String, List<ProjectEntity>> allProjectsForAllChildRegion = new HashMap<>();

    public static List<ProjectEntity> getProjectDetails(List<String> projects) throws Exception {
        List<ProjectEntity> projectEntities = new ArrayList<>();
        List<String> missingProjects = new ArrayList<>();

        for (String org : projects) {
            if (projectMap.containsKey(org)) {
                projectEntities.add(projectMap.get(org));
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
        ProjectEntity projectEntity = null;

        if (projectMap.containsKey(projectId)) {
            projectEntity = projectMap.get(projectId);
        } else {
            projectEntity = new SecurityProjectDAL().getProject(projectId);
            projectMap.put(projectEntity.getUuid(), projectEntity);
        }

        CacheManager.startScheduler();

        return projectEntity;

    }

    public static JsonProject getJsonProjectDetails(String projectId) throws Exception {
        JsonProject project = null;

        if (jsonProjectMap.containsKey(projectId)) {
            project = jsonProjectMap.get(projectId);
        } else {
            project = new SecurityProjectDAL().getFullProjectJson(projectId);
            jsonProjectMap.put(project.getUuid(), project);
        }

        CacheManager.startScheduler();

        return project;

    }

    public static String getProjectApplicationPolicy(String projectId) throws Exception {
        String foundPolicy = null;

        if (projectApplicationPolicyMap.containsKey(projectId)) {
            foundPolicy = projectApplicationPolicyMap.get(projectId);
        } else {
            ProjectApplicationPolicyEntity policyApp = new SecurityProjectApplicationPolicyDAL().getProjectApplicationPolicyId(projectId);
            foundPolicy = policyApp.getApplicationPolicyId();
            projectApplicationPolicyMap.put(projectId, foundPolicy);
        }

        CacheManager.startScheduler();

        return foundPolicy;
    }

    public static List<ProjectEntity> getAllProjectsForAllChildRegions(String regionId) throws Exception {
        List <ProjectEntity> allDSAs = new ArrayList<>();

        if (allProjectsForAllChildRegion.containsKey(regionId)) {
            allDSAs = allProjectsForAllChildRegion.get(regionId);
        } else {
            allDSAs = new SecurityProjectDAL().getProjectsForRegion(regionId);
            allProjectsForAllChildRegion.put(regionId, allDSAs);
        }

        CacheManager.startScheduler();

        return allDSAs;
    }

    public static void clearProjectCache(String projectId) throws Exception {
        if (projectMap.containsKey(projectId)) {
            projectMap.remove(projectId);
        }

        if (jsonProjectMap.containsKey(projectId)) {
            jsonProjectMap.remove(projectId);
        }

        if (projectApplicationPolicyMap.containsKey(projectId)) {
            projectApplicationPolicyMap.remove(projectId);
        }

        allProjectsForAllChildRegion.clear();
    }

    public static void flushCache() throws Exception {
        projectMap.clear();
        jsonProjectMap.clear();
        projectApplicationPolicyMap.clear();
        allProjectsForAllChildRegion.clear();
    }
}
