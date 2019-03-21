package org.endeavourhealth.common.security.usermanagermodel.models.json;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.OrganisationEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonProject;

import java.util.ArrayList;
import java.util.List;

public class JsonUserOrganisationProject {

    private OrganisationEntity organisation = null;
    private List<JsonProject> projects = new ArrayList<>();

    public OrganisationEntity getOrganisation() {
        return organisation;
    }

    public void setOrganisation(OrganisationEntity organisation) {
        this.organisation = organisation;
    }

    public List<JsonProject> getProjects() {
        return projects;
    }

    public void setProjects(List<JsonProject> projects) {
        this.projects = projects;
    }

    public void addProject(JsonProject project) {
        if (this.projects == null) {
            this.projects = new ArrayList<>();
        }

        this.projects.add(project);
    }
}
