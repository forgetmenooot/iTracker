package com.bugztracker.web.helpers;

import com.bugztracker.commons.bean.ProjectBean;
import com.bugztracker.commons.entity.project.Project;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectManagerHelper {

    public Response getProjects(List<Project> projects) {
        Response response = new Response();

        if (projects.isEmpty()) {
            response.add("message_projects", "No projects found!");
        }

        return response;
    }

    public Response archive(boolean archive) {
        Response response = new Response();

        if (!archive) {
            response.add("error", "Some problems encountered during archiving project, please try again");
        }

        return response;
    }

    public Response checkForExistProjectAndConvert(ProjectBean projectBean, Optional<Project> projectExist) {
        Response response = new Response();

        if (projectExist.isPresent()
                && projectBean.getId() == null) {
            response.add("error", "Project with this name already exists ");
            return response;
        }

        List<String> userIds = new ArrayList<>();
        userIds.addAll(projectBean.getUserIds());

        Project project = new Project();
        project.setId(projectBean.getId());
        project.setName(projectBean.getName());
        project.setDescription(projectBean.getDescription());

        response.add("project", project);
        response.add("userIds", userIds);

        return response;
    }
}
