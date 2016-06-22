package com.bugztracker.web.controllers;

import com.bugztracker.commons.bean.CommentBean;
import com.bugztracker.commons.entity.issue.Comment;
import com.bugztracker.commons.validators.ICommonsValidator;
import com.bugztracker.service.IIssueService;
import com.bugztracker.web.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CommentController {

    @Autowired
    @Qualifier("commonsEntityValidator")
    private ICommonsValidator commentValidator;

    @Autowired
    private IIssueService issueService;

    @MessageMapping("/commenthandleradd")
    @SendTo("/add")
    public Comment sendMessage(@RequestBody CommentBean comment) {
        commentValidator.validate(comment);
        issueService.addComment(comment.getComment(), comment.getIssueId(), comment.getUserId());
        return issueService.getLastOne(comment.getIssueId());
    }

    @MessageMapping("/commenthandlerupdate")
    @SendTo("/edit")
    public Comment editMessage(@RequestBody CommentBean comment) {
        commentValidator.validate(comment);
        issueService.updateComment(comment.getCommentId(), comment.getComment(), comment.getIssueId(), comment.getUserId());
        return issueService.getById(comment.getIssueId(), comment.getCommentId());
    }

    @MessageMapping("/commenthandlerremove")
    @SendTo("/remove")
    public Comment removeMessage(@RequestBody CommentBean comment) {
        commentValidator.validate(comment);
        Comment comment1 = issueService.getById(comment.getIssueId(), comment.getCommentId());
        issueService.removeComment(comment.getIssueId(), comment.getCommentId());
        return comment1;
    }

    @MessageExceptionHandler
    @SendToUser(value = "/errors", broadcast = false)
    public String handleException(ValidationException exc) {
        return exc.getMessage();
    }

}
