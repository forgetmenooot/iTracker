package com.bugztracker.service;

import com.bugztracker.commons.entity.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Author: Yuliia Vovk
 * Date: 04.11.15
 * Time: 10:56
 */
public interface IUserService extends IService<User> {

    Optional<User> find(String email);
    void update(User user);
    void create(User user);
    Optional<User> getByRegistrationToken(String token);
    List<User> getByProject(String projectId);
    List<User> getByProjectAndQuery(String projectId, String query);
    List<User> findAllUserNames(String query);
    void removeUsersWithRegistrationDatePassed();
}
