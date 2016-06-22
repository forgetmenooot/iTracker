package com.bugztracker.service;

import com.bugztracker.commons.entity.issue.Comment;
import com.bugztracker.commons.entity.issue.Issue;

import java.util.List;
import java.util.Optional;

public interface IIssueService extends IService<Issue> {

    int getCountOfIssuesByProjectAndStatus(String projectId, String status);
    Optional<List<Issue>> getByProjectAndStatus(String projectId, String status);
    List<Issue> getByProjectAndUserAndStatus(String projectId, String status, String userId);
    List<Issue> getByProjectId(String projectId);
    void create(Issue issue, String id);
    void update(Issue issue);
    void delete(String id);
    void addComment(String comment, String issueId, String senderId);
    Comment getLastOne(String issueId);
    void updateComment(String commentId, String comment, String issueId, String senderId);
    Comment getById(String issueId, String commentId);
    void removeComment(String issueId, String commentId);
    List<String> getAttachments(String issueId);
    void addAttachment(String issueId, String pathToFile);
    boolean checkIfPathExists(String issueId, String pathToFile);
    void deleteAttachment(String issueId, String pathToFile);
}
