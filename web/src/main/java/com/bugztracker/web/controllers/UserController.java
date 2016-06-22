package com.bugztracker.web.controllers;

import com.bugztracker.commons.entity.user.User;
import com.bugztracker.commons.validators.ICommonsValidator;
import com.bugztracker.service.IEmailService;
import com.bugztracker.service.IUserService;
import com.bugztracker.web.exception.ValidationException;
import com.bugztracker.web.helpers.Response;
import com.bugztracker.web.helpers.UserManageHelper;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserManageHelper manageHelper;

    @Autowired
    @Qualifier("commonsEntityValidator")
    private ICommonsValidator userValidator;

    @Autowired
    private IEmailService emailService;

    @Value("${web.is.register.token:false}")
    private boolean isRegisterToken;

    @Value("${web.session.time:30}")
    private int sessionTime;

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody User credentials,
                                HttpSession session) {
        Optional<User> user = userService.find(credentials.getEmail());
        Response response = manageHelper.login(user, credentials);

        if (response.contains("error")) {
            return new ResponseEntity<Object>(response.getResponse(), HttpStatus.UNAUTHORIZED);
        }

        User userToLogin = user.get();
        if (credentials.isRemember()) {
            session.setMaxInactiveInterval(Weeks.TWO.toStandardSeconds().getSeconds());
            userToLogin.setDateExpired(DateTime.now().plusWeeks(2).toDate());
            userService.update(userToLogin);
        }

        session.setAttribute("fullName", userToLogin.getFullName());
        session.setAttribute("userId", userToLogin.getId());

        return new ResponseEntity<Object>(response.getResponse(), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody User newUser) {
        userValidator.validate(newUser);
        Optional<User> user = userService.find(newUser.getEmail());
        Response response = manageHelper.register(user, newUser, isRegisterToken, sessionTime);

        if (response.contains("error")) {
            return new ResponseEntity<Object>(response.getResponse(), HttpStatus.CONFLICT);
        }

        User userToRegister = (User) response.get("user");
        userService.create(userToRegister);

        if (isRegisterToken) {
            emailService.sendRegisterEmail(userToRegister);
        }

        return new ResponseEntity<Object>(response.getResponse(), HttpStatus.OK);
    }

    @RequestMapping(value ="/auth/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        String userId = (String) session.getAttribute("userId");

        Optional<User> user = userService.get(userId);
        if(user.isPresent()) {
            user.get().setDateExpired(null);
            userService.update(user.get());
        }

        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(value = "/auth/activate/{token}", method = RequestMethod.GET)
    public ModelAndView activateAccount(@PathVariable String token) {
        Optional<User> userByToken = userService.getByRegistrationToken(token);
        Response response = manageHelper.activateAccount(userByToken);

        if (response.contains("user")) {
            userService.update((User) response.get("user"));
        }

        return new ModelAndView("result", response.getResponse());
    }

    @RequestMapping(value = "/projects/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity getParticipantsOfProject(@PathVariable String id) {
        List<User> userList = userService.getByProjectId(id);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{id}/participants", method = RequestMethod.GET, params = {"name"})
    public ResponseEntity getParticipantsOfProjectByName(@PathVariable String id, @RequestParam String name) {
        List<User> userList = userService.getByProjectIdAndName(id, name);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity getUsers(@RequestParam String name) {
        List<User> userNames = userService.findAllUsersByName(name);
        return new ResponseEntity<Object>(userNames, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity handleException(ValidationException exc) {
        Response response = new Response();
        response.add("error", exc.getMessage());
        return new ResponseEntity<Object>(response.getResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value ={RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleError(HttpServletRequest req, Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return "not-found";
    }

}
