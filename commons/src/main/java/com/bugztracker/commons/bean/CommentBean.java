package com.bugztracker.commons.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("comment", comment)
                .append("issueId", issueId)
                .append("userId", userId)
                .append("commentId", commentId)
                .toString();
    }
}
