package com.bugztracker.service.exception;

public class IssueCommentServiceException extends RuntimeException {

    public IssueCommentServiceException() {
    }

    public IssueCommentServiceException(String message) {
        super(message);
    }

    public IssueCommentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public IssueCommentServiceException(Throwable cause) {
        super(cause);
    }
}
