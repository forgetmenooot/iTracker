package com.bugztracker.service;

import com.bugztracker.commons.entity.user.User;

public interface IEmailService {

    void sendRegisterEmail(User sendTo);

}
