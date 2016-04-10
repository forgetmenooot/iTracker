package com.bugztracker.persistence.dao.impl;

import com.bugztracker.commons.entity.project.Project;
import com.bugztracker.persistence.dao.BaseDao;
import com.bugztracker.persistence.dao.IProjectRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Oleh_Osyka
 * Date: 14.02.2016
 * Time: 17:11
 */
@Repository
public class ProjectRepository extends BaseDao<Project> implements IProjectRepository {

    public static final String COLLECTION_NAME = "Projects";

    protected ProjectRepository() {
        super(COLLECTION_NAME, Project.class);
    }

    @Override
    protected void createIndex() {
        createIndex(new Index("name", Sort.Direction.ASC).unique());
    }

    @Override
    public Project findByProjectName(String name) {
        return findOne(query(where("name").is(name)));
    }

    @Override
    public List<Project> findProjectsByQuery(String query) {
        return findAll(query(where("name").regex(Pattern.compile(query, Pattern.CASE_INSENSITIVE))));
    }

}
