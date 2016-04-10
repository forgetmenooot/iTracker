package com.bugztracker.persistence.dao.impl;

import com.bugztracker.commons.entity.user.User;
import com.bugztracker.commons.utils.DbUtils;
import com.bugztracker.persistence.dao.BaseDao;
import com.bugztracker.persistence.dao.IUserRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Oleh_Osyka
 * Date: 13.02.2016
 * Time: 18:40
 */
@Repository
public class UserRepository extends BaseDao<User> implements IUserRepository {

    public static final String COLLECTION_NAME = "Users";

    protected UserRepository() {
        super(COLLECTION_NAME, User.class);
    }

    @Override
    public void createIndex() {
        createIndex(new Index("email", Sort.Direction.ASC).unique());
        createIndex(new Index("fullName", Sort.Direction.ASC));
    }

    @Override
    public User find(String email) {
        return findOne(query(where("email").is(email)));
    }

    @Override
    public List<User> findById(List<String> ids) {
        return findAll(query(where("_id").in(DbUtils.convertIds(ids))));
    }

    @Override
    public List<User> findByFullName(String query) {
        return findAll(query(where("fullName").regex(Pattern.compile(query, Pattern.CASE_INSENSITIVE))));
    }

    @Override
    public List<User> findUsersByProjectAndQuery(String projectId, String query) {
        return findAll(query(where("projects._id").is(new ObjectId(projectId)).and("fullName")
                .regex(Pattern.compile(query, Pattern.CASE_INSENSITIVE))));
    }

    @Override
    public User findUserByRegistrationToken(String registrationToken) {
        return findOne(query(where("registrationToken").is(registrationToken)));
    }

    @Override
    public void removeUsersWithRegistrationDatePassed(Date date) {
        mongo().remove(query(where("dueRegisterDate").lt(date)), COLLECTION_NAME);
    }

    @Override
    public List<User> getByProjectId(String projectId) {
        return findAll(query(where("projects._id").is(new ObjectId(projectId))));
    }

}
