package com.bugztracker.web.helpers;

import com.bugztracker.commons.bean.ProjectBean;
import com.bugztracker.commons.entity.project.Project;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bugztracker.web.Constants.MESSAGE_PROJECT_NAMES;
import static com.bugztracker.web.Constants.VIEW_ERROR;

/**
 * Author: Yuliia Vovk
 * Date: 21.02.16
 * Time: 12:21
 */
@Component
public class ProjectManagerHelper {

    public Response getProjects(Optional<List<Project>> projects) {
        Response response = new Response();

        if (!projects.isPresent() || projects.get().isEmpty()) {
            response.add(MESSAGE_PROJECT_NAMES, "No projects found!");
        }

        return response;
    }

    public Response archive(boolean archive) {
        Response response = new Response();

        if (!archive) {
            response.add(VIEW_ERROR, "Some problems encountered during archiving project, please try again");
        }

        return response;
    }

    public Response checkForExistProjectAndConvert(ProjectBean projectBean, Optional<Project> projectExist) {
        Response response = new Response();

        if (projectExist.isPresent()
                && (projectBean.getName().equals(projectExist.get().getName())
                && projectBean.getId() == null)) {
            response.add(VIEW_ERROR, "Project with this name already exists! ");
            return response;
        }

        List<String> userIds = new ArrayList<>();
        if (projectBean.getUserIds() != null || !projectBean.getUserIds().isEmpty()) {
            userIds.addAll(projectBean.getUserIds());
        }

        Project project = new Project();
        project.setId(projectBean.getId());
        project.setName(projectBean.getName());
        project.setDescription(projectBean.getDescription());

        response.add("project", project);
        response.add("userIds", userIds);

        return response;
    }
}
