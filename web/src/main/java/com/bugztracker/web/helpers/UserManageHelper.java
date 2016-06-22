package com.bugztracker.web.helpers;

import com.bugztracker.commons.entity.user.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserManageHelper {

    public Response login(Optional<User> actual, User credentials) {
        Response response = new Response();
        if (!actual.isPresent()) {
            response.add("error", "No user found with such login!");
            return response;
        }

        User credentialsUser = actual.get();
        String md5Hash = DigestUtils.md5Hex(credentials.getPassword());
        if (!md5Hash.startsWith(credentialsUser.getPassword())) {
            response.add("error", "Incorrect password! Please, check and try again!");
            return response;
        }

        if (!credentialsUser.isActive()) {
            response.add("error", "Your account has not been activated!");
            return response;
        }

        return response;
    }

    public Response register(Optional<User> foundUser, User newUser, boolean isRegisterToken, int sessionTime) {
        Response response = new Response();
        if (foundUser.isPresent() && !foundUser.get().equals(newUser)) {
            response.add("error", "Email has already been registered! ");
            return response;
        }

        if (foundUser.isPresent() && foundUser.get().equals(newUser)) {
            response.add("error", "Registration link has already been sent to you! ");
            return response;
        }

        String md5Hash = DigestUtils.md5Hex(newUser.getPassword());
        newUser.setPassword(md5Hash.substring(0, 10));
        if (isRegisterToken) {
            newUser.setActive(false);
            newUser.setDueRegisterDate(DateTime.now().plusMinutes(sessionTime).toDate());

            String registrationToken = UUID.randomUUID().toString().substring(0, 15);
            newUser.setRegistrationToken(registrationToken);

            response.add("success", "Email with activation link has been sent! Please, follow it to succeed in signing up!");
        } else {
            newUser.setActive(true);
            newUser.setRegistrationToken(null);
            newUser.setDueRegisterDate(null);
        }
        newUser.setProjects(new ArrayList<>());
        response.add("user", newUser);
        return response;
    }

    public Response activateAccount(Optional<User> userByToken) {
        Response response = new Response();
        if (!userByToken.isPresent() || new Timestamp(DateTime.now().getMillis()).after(userByToken.get().getDueRegisterDate())) {
            response.add("error", "Activation has been already expired or is broken! ");
        } else {
            User user = userByToken.get();
            user.setActive(true);
            user.setRegistrationToken(null);
            user.setDueRegisterDate(null);

            response.add("user", user);
            response.add("success", "Your account has been activated! ");
        }
        return response;
    }

}
