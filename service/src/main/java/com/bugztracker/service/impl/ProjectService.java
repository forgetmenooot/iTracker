package com.bugztracker.service.impl;

import com.bugztracker.commons.entity.project.Project;
import com.bugztracker.commons.entity.user.User;
import com.bugztracker.persistence.dao.IProjectRepository;
import com.bugztracker.persistence.dao.IUserRepository;
import com.bugztracker.service.IProjectService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author: Yuliia Vovk
 * Date: 21.02.16
 * Time: 12:15
 */
@Service
public class ProjectService implements IProjectService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public void update(Project project, List<String> userIds) {
        try {
            if (project.getId() == null || project.getId().isEmpty()) {
                project.setDate(DateTime.now().toDate());
                project.setArchived(false);
                projectRepository.add(project);

                userRepository.findById(userIds).forEach(user -> {
                    if (user.getProjects() != null || !user.getProjects().isEmpty()) {
                        user.getProjects().add(project);
                    } else {
                        user.setProjects(new ArrayList<>());
                        user.getProjects().add(project);
                    }
                    userRepository.update(user);
                });
            } else {
                Project pr = projectRepository.get(project.getId());
                pr.setDescription(project.getDescription());
                pr.setName(project.getName());

                projectRepository.update(pr);

                //update in issue

                List<User> userList = userRepository.getByProjectId(pr.getId());
                if (userList.size() > userIds.size()) {
                    List<String> userListIds = new ArrayList<>();
                    userList.forEach(user -> userListIds.add(user.getId()));
                    userListIds.removeAll(userIds);
                    userListIds.forEach(id -> {
                        User u = userRepository.get(id);
                        u.getProjects().remove(pr);
                        userRepository.update(u);
                    });
                }

                if (!userIds.isEmpty()) {
                    userRepository.findById(userIds).forEach(user -> {
                        List<Project> userProjects = user.getProjects();
                        if (userProjects != null || !userProjects.isEmpty()) {
                            if (userProjects.contains(pr)) {
                                userProjects.forEach(proj -> {
                                    if (proj.getId().equals(pr.getId())) {
                                        proj.setDescription(pr.getDescription());
                                        proj.setName(pr.getName());
                                    }
                                });
                            } else {
                                userProjects.add(pr);
                            }
                            user.setProjects(userProjects);
                        } else {
                            user.setProjects(new ArrayList<>());
                            user.getProjects().add(pr);
                        }
                        userRepository.update(user);
                    });
                }
            }
        } catch (ConstraintViolationException cve) {
            LOG.error("Creating or updating project with name = %s failed, %s", project.getName(), cve.getMessage());
        }
    }

    @Override
    public Optional<Project> getByName(String projectName) {
        return Optional.ofNullable(projectRepository.findByProjectName(projectName));
    }

    @Override
    public Optional<List<Project>> getByUserId(String userId) {
        List<Project> projects = userRepository.get(userId).getProjects();
        projects.removeIf(Project::isArchived);
        return Optional.ofNullable(projects);
    }

    @Override
    public boolean archive(String id) {
        if (id == null || id.isEmpty() || projectRepository.get(id) == null) {
            return false;
        }

        Project project = projectRepository.get(id);
        project.setArchived(true);
        projectRepository.update(project);

        List<User> users = userRepository.getByProjectId(id);
        users.forEach(user -> user.getProjects().forEach(pr -> {
            if (pr.getId().equals(id)) {
                pr.setArchived(true);
            }
        }));
        users.forEach(user -> userRepository.update(user));

        return true;
    }

    @Override
    public List<Project> getProjectsByQuery(String query) {
        return projectRepository.findProjectsByQuery(query);
    }

    @Override
    public boolean checkIfProjectOfUser(String projectId, String userId) {
        User user = userRepository.get(userId);
        return user.getProjects().removeIf(project -> project.getId().equals(projectId));
    }

    @Override
    public Optional<Project> get(String id) {
        return Optional.ofNullable(projectRepository.get(id));
    }
}
