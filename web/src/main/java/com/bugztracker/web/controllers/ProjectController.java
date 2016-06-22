package com.bugztracker.web.controllers;

import com.bugztracker.commons.bean.ProjectBean;
import com.bugztracker.commons.entity.project.Project;
import com.bugztracker.commons.validators.ICommonsValidator;
import com.bugztracker.service.IProjectService;
import com.bugztracker.web.exception.ValidationException;
import com.bugztracker.web.helpers.ProjectManagerHelper;
import com.bugztracker.web.helpers.Response;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @Autowired
    @Qualifier("commonsEntityValidator")
    private ICommonsValidator projectValidator;

    @Autowired
    private ProjectManagerHelper managerHelper;

    @RequestMapping(value = "/users/{id}/projects", method = RequestMethod.GET)
    public ResponseEntity getProjectNames(@PathVariable String id) {
        List<Project> projectList = projectService.getByUserId(id);

        Response response = managerHelper.getProjects(projectList);

        if (!response.isEmpty()) {
            return new ResponseEntity<>(response.getResponse(), HttpStatus.OK);
        }

        return new ResponseEntity<>(projectList, HttpStatus.OK);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody ProjectBean project) {

        projectValidator.validate(project);

        Response response = managerHelper.checkForExistProjectAndConvert(project,
                projectService.getByName(project.getName()));

        if (response.get("error") != null) {
            return new ResponseEntity<>(response.getResponse(), HttpStatus.BAD_REQUEST);
        }

        projectService.update((Project) response.get("project"), (List<String>) response.get("userIds"));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody ProjectBean project) {

        projectValidator.validate(project);

        Response response = managerHelper.checkForExistProjectAndConvert(project,
                projectService.getByName(project.getName()));

        if (response.get("error") != null) {
            return new ResponseEntity<>(response.getResponse(), HttpStatus.BAD_REQUEST);
        }

        projectService.create((Project) response.get("project"), (List<String>) response.get("userIds"));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET, params = {"name"})
    public ResponseEntity getProjects(@RequestParam String name) {
        List<Project> projects = projectService.getProjectsByName(name);

        return new ResponseEntity<Object>(projects, HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{name}", method = RequestMethod.GET)
    public ResponseEntity getProjectByName(@PathVariable String name) {
        Optional<Project> project = projectService.getByName(name);
        Response response = new Response();

        if(!project.isPresent()) {
            response.add("error", "No projects with such full name found! ");
            return new ResponseEntity<Object>(response.getResponse(), HttpStatus.BAD_REQUEST);
        }

        if(project.isPresent() && project.get().isArchived()) {
            response.add("error", "Project is archived! ");
            return new ResponseEntity<Object>(response.getResponse(), HttpStatus.BAD_REQUEST);
        }

        response.add("projectId", project.get().getId());
        return new ResponseEntity<Object>(response.getResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{id}/archive", method = RequestMethod.PUT)
    public ResponseEntity archive(@PathVariable String id) {
        Response response = managerHelper.archive(projectService.archive(id));
        if(!response.isEmpty()) {
            return new ResponseEntity<>(response.getResponse(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/projects/{pid}/check/{uid}", method = RequestMethod.GET)
    public Boolean checkUserForProjectHaving(@PathVariable String uid, @PathVariable String pid) {
        return projectService.checkIfProjectOfUser(pid, uid);
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
