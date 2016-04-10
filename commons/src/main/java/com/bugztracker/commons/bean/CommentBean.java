package com.bugztracker.commons.bean;

/**
 * Author: Yuliia Vovk
 * Date: 03.04.16
 * Time: 22:45
 */
public class CommentBean {

    private String comment;
    private String issueId;
    private String userId;
    private String commentId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
