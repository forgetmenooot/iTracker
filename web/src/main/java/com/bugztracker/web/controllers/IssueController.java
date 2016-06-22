package com.bugztracker.web.controllers;

import com.bugztracker.commons.entity.issue.Issue;
import com.bugztracker.commons.validators.ICommonsValidator;
import com.bugztracker.service.IIssueService;
import com.bugztracker.web.exception.ValidationException;
import com.bugztracker.web.helpers.IssueManagerHelper;
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
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class IssueController {

    @Autowired
    private IIssueService issueService;

    @Autowired
    private IssueManagerHelper managerHelper;

    @Autowired
    @Qualifier("commonsEntityValidator")
    private ICommonsValidator issueValidator;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/projects/{id}/tickets/count", method = RequestMethod.GET)
    public Integer getCountOfTasksByStatus(@PathVariable String id, @RequestParam String status) {
        return issueService.getCountOfIssuesByProjectAndStatus(id, status);
    }

    @RequestMapping(value = "/projects/{id}/tickets", method = RequestMethod.GET, params = "status")
    public ResponseEntity getTasksByStatus(@PathVariable String id, @RequestParam String status,
                                                  @RequestParam(value = "assignee_id", required = false) String assigneeId) {
        if (assigneeId != null) {

            List<Issue> issueList = issueService.getByProjectAndUserAndStatus(id, status, assigneeId);

            Response response = managerHelper.getIssuesByStatusAndProjectAndUser(issueList, status);

            if (!response.isEmpty()) {
                return new ResponseEntity<>(response.getResponse(), HttpStatus.OK);
            }
            return new ResponseEntity<>(issueList, HttpStatus.OK);
        } else {
            Optional<List<Issue>> issueList = issueService.getByProjectAndStatus(id, status);
            return new ResponseEntity<>(issueList.get(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/projects/{id}/tickets", method = RequestMethod.GET)
    public ResponseEntity getAllIssues(@PathVariable String id) {
        List<Issue> issueList = issueService.getByProjectId(id);
        return new ResponseEntity<>(issueList, HttpStatus.OK);
    }

    @RequestMapping(value = "/projects/{id}/tickets", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody Issue issue, HttpSession session) {

        issueValidator.validate(issue);

        issueService.update(issue);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/projects/{id}/tickets", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody Issue issue, HttpSession session) {

        issueValidator.validate(issue);

        String id = (String) session.getAttribute("userId");

        issueService.create(issue, id);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "projects/{pid}/tickets/{iid}", method = RequestMethod.DELETE)
    public void deleteProject(@PathVariable String iid, @PathVariable String pid) {
        issueService.delete(iid);
    }


    @RequestMapping(value = "/projects/{pid}/tickets/{iid}", method = RequestMethod.GET)
    public ResponseEntity getIssue(@PathVariable String pid, @PathVariable String iid ) {
        return new ResponseEntity<>(issueService.get(iid).get(), HttpStatus.OK);
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
