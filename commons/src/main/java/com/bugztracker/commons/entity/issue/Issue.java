package com.bugztracker.commons.entity.issue;

import com.bugztracker.commons.entity.project.Project;
import com.bugztracker.commons.entity.user.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Issue implements Serializable {

    @Id
    private String id;

    @NotBlank(message = "Name is required! ")
    @Size(max = 50, message = "Please, shorten the name of issue. Not more than 50 symbols is possible!")
    private String name;

    @Digits(integer = 2, fraction = 1, message = "Version must be a float number from 1 to 10! ")
    @Range(min = 1, max = 10, message = "Version must be a float number from 1 to 10! ")
    @NotNull(message = "Version is required and must be a float number from 1 to 10!")
    private Double version;
    private Date creationDate;
    private Date lastUpdateDate;

    @NotNull(message = "Priority is required! ")
    private Priority priority;

    private Status status = Status.OPENED;

    private String description;

    @NotNull(message = "Category is required! ")
    private Category category;

    private User creator;

    @NotNull(message = "Assignee is required! ")
    private User assignee;
    private Project project;
    private List<String> attachmentPaths = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        return new EqualsBuilder()
                .append(id, issue.id)
                .append(name, issue.name)
                .append(version, issue.version)
                .append(creationDate, issue.creationDate)
                .append(lastUpdateDate, issue.lastUpdateDate)
                .append(priority, issue.priority)
                .append(status, issue.status)
                .append(description, issue.description)
                .append(category, issue.category)
                .append(creator, issue.creator)
                .append(assignee, issue.assignee)
                .append(project, issue.project)
                .append(attachmentPaths, issue.attachmentPaths)
                .append(comments, issue.comments)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(version)
                .append(creationDate)
                .append(lastUpdateDate)
                .append(priority)
                .append(status)
                .append(description)
                .append(category)
                .append(creator)
                .append(assignee)
                .append(project)
                .append(attachmentPaths)
                .append(comments)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("version", version)
                .append("creationDate", creationDate)
                .append("lastUpdateDate", lastUpdateDate)
                .append("priority", priority)
                .append("status", status)
                .append("description", description)
                .append("category", category)
                .append("creator", creator)
                .append("assignee", assignee)
                .append("project", project)
                .append("attachmentPaths", attachmentPaths)
                .append("comments", comments)
                .toString();
    }
}
