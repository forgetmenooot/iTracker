package com.bugztracker.web.helpers;

import com.bugztracker.commons.entity.issue.Issue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IssueManagerHelper {

    public Response getIssuesByStatusAndProjectAndUser(List<Issue> issues, String status) {
        Response response = new Response();
        if (issues.isEmpty()) {
            response.add("message_status", "No tasks with status " + status + " found");
        }

        return response;
    }

}
