package com.bugztracker.service.impl;

import com.bugztracker.commons.entity.issue.Issue;
import com.bugztracker.commons.entity.project.Project;
import com.bugztracker.commons.entity.user.User;
import com.bugztracker.persistence.dao.IIssueRepository;
import com.bugztracker.persistence.dao.IProjectRepository;
import com.bugztracker.persistence.dao.IUserRepository;
import com.bugztracker.persistence.exception.DBException;
import com.bugztracker.service.IProjectService;
import com.bugztracker.service.exception.ServiceException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IIssueRepository issueRepositories;

    @Override
    public Optional<Project> update(Project project, List<String> userIds) {
        try {
            Project pr = projectRepository.get(project.getId());
            pr.setDescription(project.getDescription());
            pr.setName(project.getName());

            projectRepository.update(pr);

            List<Issue> issues = issueRepositories.getByProjectId(project.getId());

            issues.forEach(issue -> {
                issue.setProject(project);
                issueRepositories.update(issue);
            });

            List<User> userList = userRepository.getByProjectId(pr.getId());
            List<String> userListIds = userList.stream().map(User::getId).collect(Collectors.toList());

            if (userList.size() > userIds.size()) {
                userList.forEach(user -> {
                    user.getProjects().remove(project);
                    if (!userIds.contains(user.getId())) {
                        userRepository.update(user);
                    } else {
                        user.getProjects().add(project);
                        userRepository.update(user);
                    }
                });
            } else {
                userList.forEach(user -> {
                    user.getProjects().remove(project);
                    user.getProjects().add(project);
                    userRepository.update(user);
                });

                List<String> ids = userIds.stream().filter(userListIds::contains).collect(Collectors.toList());
                if (!ids.isEmpty()) {
                    ids.forEach(id -> {
                        User u = userRepository.get(id);
                        u.getProjects().add(project);
                        userRepository.update(u);
                    });
                }
            }

        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
        return Optional.ofNullable(project);
    }

    @Override
    public Optional<Project> create(Project project, List<String> userIds) {
        project.setDate(DateTime.now().toDate());
        project.setArchived(false);
        projectRepository.add(project);

        userRepository.findById(userIds).forEach(user -> {
            user.getProjects().add(project);
            userRepository.update(user);
        });

        return Optional.ofNullable(project);
    }

    @Override
    public Optional<Project> getByName(String projectName) {
        try {
            return Optional.ofNullable(projectRepository.findByProjectName(projectName));
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public List<Project> getByUserId(String userId) {
        try {
            List<Project> projects = userRepository.get(userId).getProjects();
            projects.removeIf(Project::isArchived);
            return projects;
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public boolean archive(String id) {
        if (id == null || id.isEmpty() || !get(id).isPresent()) {
            return false;
        }

        try {
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

        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public List<Project> getProjectsByName(String name) {
        try {
            List<Project> projects = projectRepository.findProjectsByQuery(name);
            projects.removeIf(Project::isArchived);
            return projects;
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public boolean checkIfProjectOfUser(String projectId, String userId) {
        try {
            User user = userRepository.get(userId);
            return user.getProjects().removeIf(project -> project.getId().equals(projectId));
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public Optional<Project> get(String id) {
        try {
            return Optional.ofNullable(projectRepository.get(id));
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }
}
