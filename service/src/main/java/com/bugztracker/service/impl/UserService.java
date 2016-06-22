package com.bugztracker.service.impl;

import com.bugztracker.commons.entity.user.User;
import com.bugztracker.persistence.dao.IUserRepository;
import com.bugztracker.persistence.exception.DBException;
import com.bugztracker.service.IUserService;
import com.bugztracker.service.exception.ServiceException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Optional<User> find(String email) {
        try {
            return Optional.ofNullable(userRepository.find(email));
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public Optional<User> update(User user) {
        try {
            userRepository.update(user);
            return get(user.getId());
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public Optional<User> create(User user) {
        try {
            userRepository.add(user);
            return get(user.getId());
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public Optional<User> getByRegistrationToken(String token) {
        try {
            return Optional.ofNullable(userRepository.findUserByRegistrationToken(token));
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public List<User> getByProjectId(String projectId) {
        try {
            return userRepository.getByProjectId(projectId);
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public List<User> getByProjectIdAndName(String projectId, String query) {
        try {
            return userRepository.findUsersByProjectAndQuery(projectId, query);
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public List<User> findAllUsersByName(String query) {
        try {
            return userRepository.findByFullName(query);
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public void removeUsersWithRegistrationDatePassed() {
        try {
            userRepository.removeUsersWithRegistrationDatePassed(DateTime.now().toDate());
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }

    @Override
    public Optional<User> get(String id) {
        try {
            return Optional.ofNullable(userRepository.get(id));
        } catch (DBException exc) {
            throw new ServiceException(exc);
        }
    }
}
