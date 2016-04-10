package com.bugztracker.service;

import com.bugztracker.commons.entity.project.Project;

import java.util.List;
import java.util.Optional;

/**
 * Author: Yuliia Vovk
 * Date: 21.02.16
 * Time: 12:14
 */
public interface IProjectService extends IService<Project> {

    void update(Project project, List<String> userIds);
    Optional<Project> getByName(String projectName);
    Optional<List<Project>> getByUserId(String userId);
    boolean archive(String id);
    List<Project> getProjectsByQuery(String query);
    boolean checkIfProjectOfUser(String projectId, String userId);
}
