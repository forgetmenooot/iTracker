package com.bugztracker.service;

import com.bugztracker.commons.entity.project.Project;

import java.util.List;
import java.util.Optional;

public interface IProjectService extends IService<Project> {

    Optional<Project> update(Project project, List<String> userIds);
    Optional<Project> create(Project project, List<String> userIds);
    Optional<Project> getByName(String projectName);
    List<Project> getByUserId(String userId);
    boolean archive(String id);
    List<Project> getProjectsByName(String query);
    boolean checkIfProjectOfUser(String projectId, String userId);

}
