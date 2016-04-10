package com.bugztracker.web.controllers;

import com.bugztracker.commons.entity.issue.Issue;
import com.bugztracker.commons.validators.ICommonsValidator;
import com.bugztracker.service.IIssueService;
import com.bugztracker.web.helpers.IssueManagerHelper;
import com.bugztracker.web.helpers.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Author: Yuliia Vovk
 * Date: 21.02.16
 * Time: 12:49
 */
@Controller
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
    @RequestMapping(value = "/project/{projectId}/issues/count", method = RequestMethod.GET)
    public Integer getCountOfTasksByStatus(@PathVariable String projectId, @RequestParam String status) {
        return issueService.getCountOfIssuesByProjectAndStatus(projectId, status);
    }

    @RequestMapping(value = "/project/{projectId}/issues", method = RequestMethod.GET, params = "status")
    public ResponseEntity getTasksByStatus(@PathVariable String projectId, @RequestParam String status,
                                                  @RequestParam(value = "assignee_id", required = false) String assigneeId) {
        if (assigneeId != null) {

            List<Issue> issueList = issueService.getByProjectAndUserAndStatus(projectId, status, assigneeId);

            Response response = managerHelper.getIssuesByStatusAndProjectAndUser(issueList, status);

            if (!response.isEmpty()) {
                return new ResponseEntity<>(response.getResponse(), HttpStatus.OK);
            }
            return new ResponseEntity<>(issueList, HttpStatus.OK);
        } else {
            Optional<List<Issue>> issueList = issueService.getByProjectAndStatus(projectId, status);
            return new ResponseEntity<>(issueList.get(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/project/{projectId}/issues", method = RequestMethod.GET)
    public ResponseEntity getAllIssues(@PathVariable String projectId) {
        List<Issue> issueList = issueService.getByProjectId(projectId);
        return new ResponseEntity<>(issueList, HttpStatus.OK);
    }

    @RequestMapping(value = "/issue", method = RequestMethod.PUT)
    public ResponseEntity addProject(@RequestBody Issue issue, HttpSession session) {

        issueValidator.validate(issue);

        String id = (String) session.getAttribute("id");

        issueService.create(issue, id);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/issue/{id}", method = RequestMethod.DELETE)
    public void deleteProject(@PathVariable String id) {
        issueService.delete(id);
    }


    @RequestMapping(value = "/issue/{id}", method = RequestMethod.GET)
    public ResponseEntity getIssue(@PathVariable String id) {
        return new ResponseEntity<>(issueService.get(id).get(), HttpStatus.OK);
    }

}
