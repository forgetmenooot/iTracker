package com.bugztracker.persistence.dao;

import com.bugztracker.commons.entity.user.User;

import java.util.Date;
import java.util.List;

public interface IUserRepository extends IBaseDao<User> {

    User find(String email);
    List<User> findById(List<String> ids);
    List<User> findByFullName(String regexp);
    List<User> findUsersByProjectAndQuery(String projectId, String query);
    User findUserByRegistrationToken(String registrationToken);
    void removeUsersWithRegistrationDatePassed(Date date);
    List<User> getByProjectId(String projectId);

}

