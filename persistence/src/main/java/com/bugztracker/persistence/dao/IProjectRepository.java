package com.bugztracker.persistence.dao;

import com.bugztracker.commons.entity.project.Project;

import java.util.List;

public interface IProjectRepository extends IBaseDao<Project> {

    Project findByProjectName(String name);
    List<Project> findProjectsByQuery(String query);

}
