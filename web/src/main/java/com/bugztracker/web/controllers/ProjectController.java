package com.bugztracker.web.controllers;

import com.bugztracker.commons.bean.ProjectBean;
import com.bugztracker.commons.entity.project.Project;
import com.bugztracker.commons.validators.ICommonsValidator;
import com.bugztracker.service.IProjectService;
import com.bugztracker.service.IUserService;
import com.bugztracker.web.helpers.ProjectManagerHelper;
import com.bugztracker.web.helpers.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

import static com.bugztracker.web.Constants.VIEW_ERROR;

/**
 * Author: Yuliia Vovk
 * Date: 21.02.16
 * Time: 12:19
 */
@Controller
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @Autowired
    private IUserService userService;

    @Autowired
    @Qualifier("commonsEntityValidator")
    private ICommonsValidator projectValidator;

    @Autowired
    private ProjectManagerHelper managerHelper;

    @RequestMapping(value = "/user/{userId}/projects", method = RequestMethod.GET)
    public ResponseEntity getProjectNames(@PathVariable String userId) {
        Optional<List<Project>> projectList = projectService.getByUserId(userId);

        Response response = managerHelper.getProjects(projectList);

        if (!response.isEmpty()) {
            return new ResponseEntity<>(response.getResponse(), HttpStatus.OK);
        }

        return new ResponseEntity<>(projectList.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/project", method = RequestMethod.PUT)
    public ResponseEntity addProject(@RequestBody ProjectBean project) {

        projectValidator.validate(project);

        Response response = managerHelper.checkForExistProjectAndConvert(project,
                projectService.getByName(project.getName()));

        if (response.get(VIEW_ERROR) != null) {
            return new ResponseEntity<>(response.getResponse(), HttpStatus.BAD_REQUEST);
        }

        projectService.update((Project) response.get("project"), (List<String>) response.get("userIds"));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ResponseEntity getProjects(@RequestParam String query) {
        List<Project> projects = projectService.getProjectsByQuery(query);

        return new ResponseEntity<Object>(projects, HttpStatus.OK);
    }

    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public ResponseEntity getProjectByName(@RequestParam String name) {
        Optional<Project> project = projectService.getByName(name);
        Response response = new Response();
        if(!project.isPresent()) {
            response.add(VIEW_ERROR, "Please, specify full project's name to proceed search! ");
            return new ResponseEntity<Object>(response.getResponse(), HttpStatus.BAD_REQUEST);
        }
        response.add("redirect", "/project/issues");
        response.add("projectId", project.get().getId());
        return new ResponseEntity<Object>(response.getResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/project/{id}/archive", method = RequestMethod.POST)
    public ResponseEntity archiveProject(@PathVariable String id) {
        Response response = managerHelper.archive(projectService.archive(id));
        if(!response.isEmpty()) {
            return new ResponseEntity<>(response.getResponse(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/project/check", method = RequestMethod.GET)
    public Boolean checkUserForProjectHaving(@RequestParam String userId, @RequestParam String projectId) {
        return projectService.checkIfProjectOfUser(projectId, userId);
    }

    @ExceptionHandler
    public ResponseEntity handleException(ValidationException exc) {
        Response response = new Response();
        response.add(VIEW_ERROR, exc.getMessage());
        return new ResponseEntity<Object>(response.getResponse(), HttpStatus.BAD_REQUEST);
    }
}
