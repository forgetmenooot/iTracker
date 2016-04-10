package com.bugztracker.persistence.dao;

import com.bugztracker.commons.entity.issue.Issue;

import java.util.Date;
import java.util.List;

/**
 * Author: Yuliia Vovk
 * Date: 04.11.15
 * Time: 10:56
 */
public interface IIssueRepository extends IBaseDao<Issue> {

    List<Issue> getByProjectId(String projectId);
    List<Issue> getByProjectAndStatus(String projectId, String status);
    List<Issue> getByProjectAndUserAndStatus(String projectId, String status, String assignedUserId);
    List<Issue> getByProjectAndAssignedUser(String projectName, String assignedUserEmail);
    int getCountByDateAndOpenedStatus(String projectName, Date date);
    int getCountByDateAndClosedStatus(String projectName, Date date);
}
