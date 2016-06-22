package com.bugztracker.service.impl;

import com.bugztracker.commons.entity.issue.Comment;
import com.bugztracker.commons.entity.issue.Issue;
import com.bugztracker.commons.entity.issue.Status;
import com.bugztracker.commons.entity.project.Project;
import com.bugztracker.commons.entity.user.User;
import com.bugztracker.persistence.dao.IIssueRepository;
import com.bugztracker.persistence.dao.IProjectRepository;
import com.bugztracker.persistence.dao.IUserRepository;
import com.bugztracker.service.IIssueService;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService implements IIssueService {

    @Autowired
    private IIssueRepository issueRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProjectRepository projectRepository;

    @Override
    public int getCountOfIssuesByProjectAndStatus(String projectId, String status) {
        Optional<List<Issue>> issues = getByProjectAndStatus(projectId, status);
        return issues.isPresent() ? issues.get().size() : 0;
    }

    @Override
    public Optional<List<Issue>> getByProjectAndStatus(String projectId, String status) {
        return Optional.ofNullable(issueRepository.getByProjectAndStatus(projectId, status));
    }

    @Override
    public List<Issue> getByProjectAndUserAndStatus(String projectId, String status, String userId) {
        return issueRepository.getByProjectAndUserAndStatus(projectId, status, userId);
    }

    @Override
    public List<Issue> getByProjectId(String projectId) {
        return issueRepository.getByProjectId(projectId);
    }

    @Override
    public void update(Issue issue) {
        User assignee = userRepository.get(issue.getAssignee().getId());
        Issue issue1 = issueRepository.get(issue.getId());

        issue1.setLastUpdateDate(DateTime.now().toDate());
        issue1.setAssignee(assignee);
        issue1.setCategory(issue.getCategory());
        issue1.setPriority(issue.getPriority());
        issue1.setStatus(issue.getStatus());
        issue1.setVersion(issue.getVersion());
        issue1.setDescription(issue.getDescription());
        issue.setName(issue.getName());
        issueRepository.update(issue1);
    }

    @Override
    public void create(Issue issue, String id) {
        User creator = userRepository.get(id);
        User assignee = userRepository.get(issue.getAssignee().getId());
        Project project = projectRepository.get(issue.getProject().getId());
        issue.setCreationDate(DateTime.now().toDate());
        issue.setAttachmentPaths(new ArrayList<>());
        issue.setCreator(creator);
        issue.setStatus(Status.OPENED);
        issue.setAssignee(assignee);
        issue.setProject(project);
        issue.setComments(new ArrayList<>());
        issueRepository.add(issue);
    }

    @Override
    public void delete(String id) {
        issueRepository.delete(issueRepository.get(id));
    }

    @Override
    public void addComment(String message, String issueId, String senderId) {
        Comment comment = new Comment();
        comment.setDate(DateTime.now().toDate());
        comment.setSender(userRepository.get(senderId));
        comment.setComment(message);
        comment.setId(new ObjectId().toHexString());

        Issue issue = issueRepository.get(issueId);
        List<Comment> comments = issue.getComments();
        comments.add(comment);

        issue.setComments(comments);
        issueRepository.update(issue);
    }

    @Override
    public Comment getLastOne(String issueId) {
        return issueRepository.get(issueId).getComments().get(issueRepository.get(issueId).getComments().size() - 1);
    }

    @Override
    public void updateComment(String commentId, String comment, String issueId, String senderId) {
        Issue issue = issueRepository.get(issueId);
        issue.getComments()
                .stream()
                .filter(c -> c.getId().equals(commentId))
                .forEach(c -> {
                    if (c.getId().equals(commentId)) {
                        c.setComment(comment);
                        c.setUpdateDate(DateTime.now().toDate());
                    }
                });

        issueRepository.update(issue);
    }

    @Override
    public Comment getById(String issueId, String commentId) {
        Issue issue = issueRepository.get(issueId);
        return issue.getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .get();
    }

    @Override
    public void removeComment(String issueId, String commentId) {
        Comment comment = getById(issueId, commentId);
        Issue issue = issueRepository.get(issueId);
        issue.getComments().remove(comment);
        issueRepository.update(issue);
    }

    @Override
    public List<String> getAttachments(String issueId) {
        return issueRepository.get(issueId).getAttachmentPaths();
    }

    @Override
    public void addAttachment(String issueId, String pathToFile) {
        Issue issue = issueRepository.get(issueId);
        issue.getAttachmentPaths().add(pathToFile);
        issueRepository.update(issue);
    }

    @Override
    public boolean checkIfPathExists(String issueId, String pathToFile) {
        return issueRepository.get(issueId).getAttachmentPaths()
                .stream()
                .anyMatch(path -> path.equals(pathToFile));
    }

    @Override
    public void deleteAttachment(String issueId, String pathToFile) {
        Issue issue = issueRepository.get(issueId);
        issue.getAttachmentPaths().remove(pathToFile);
        issueRepository.update(issue);
    }

    @Override
    public Optional<Issue> get(String id) {
        return Optional.ofNullable(issueRepository.get(id));
    }
}
