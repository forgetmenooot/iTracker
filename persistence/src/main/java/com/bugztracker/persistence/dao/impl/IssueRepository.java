package com.bugztracker.persistence.dao.impl;

import com.bugztracker.commons.entity.issue.Issue;
import com.bugztracker.commons.entity.issue.Status;
import com.bugztracker.persistence.dao.BaseDao;
import com.bugztracker.persistence.dao.IIssueRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class IssueRepository extends BaseDao<Issue> implements IIssueRepository {

    private static final String COLLECTION_NAME = "Issues";

    protected IssueRepository() {
        super(COLLECTION_NAME, Issue.class);
    }

    @Override
    protected void createIndex() {
        createIndex(new Index("name", Sort.Direction.ASC).unique());
        createIndex(new Index("priority", Sort.Direction.ASC).on("status", Sort.Direction.ASC).on("category", Sort.Direction.ASC).sparse());
        createIndex(new Index("projects.name", Sort.Direction.ASC).on("assignee.email", Sort.Direction.ASC).sparse());
    }

    @Override
    public List<Issue> getByProjectId(String projectId) {
        return findAll(query(where("project._id").is(new ObjectId(projectId))));
    }

    @Override
    public List<Issue> getByProjectAndStatus(String projectId, String status) {
        return findAll(query(where("project._id").is(new ObjectId(projectId)).and("status").is(status)));
    }

    @Override
    public List<Issue> getByProjectAndUserAndStatus(String projectId, String status, String assignedUserId) {
        return findAll(query(where("project._id").is(new ObjectId(projectId)).and("assignee._id").is(new ObjectId(assignedUserId)).and("status").is(status)));
    }

    @Override
    public List<Issue> getByProjectAndAssignedUser(String projectName, String assignedUserEmail) {
        return findAll(query(where("projectName").is(projectName).and("assigneeEmail").is(assignedUserEmail)));
    }

    @Override
    public int getCountByDateAndOpenedStatus(String projectName, Date date) {
        return findAll(query(where("projectName").is(projectName).and("status").is(Status.OPENED).and("creationDate").is(date))).size();
    }

    @Override
    public int getCountByDateAndClosedStatus(String projectName, Date date) {
        return findAll(query(where("projectName").is(projectName).and("status").is(Status.CLOSED).and("creationDate").is(date))).size();
    }
}
