package com.bugztracker.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getSignInPage() {
        return "signin";
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.GET)
    public String getSignInPageTwo() {
        return "signin";
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.GET)
    public String getSignUpPage() {
        return "signup";
    }

    @RequestMapping(value = "/projects/{id}/tickets", method = RequestMethod.GET)
    public String getProjectPage(@PathVariable String id) {
        return "issues";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String getMainPage() {
        return "dashboard";
    }

    @RequestMapping(value = "/projects/{pid}/tickets/{iid}", method = RequestMethod.GET)
    public String getNewIssuePage(@PathVariable String pid, @PathVariable String iid) {
        return "issue";
    }

}
