package com.bugztracker.service;

import com.bugztracker.commons.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IService<User> {

    Optional<User> find(String email);
    Optional<User> update(User user);
    Optional<User> create(User user);
    Optional<User> getByRegistrationToken(String token);
    List<User> getByProjectId(String projectId);
    List<User> getByProjectIdAndName(String projectId, String name);
    List<User> findAllUsersByName(String name);
    void removeUsersWithRegistrationDatePassed();

}
