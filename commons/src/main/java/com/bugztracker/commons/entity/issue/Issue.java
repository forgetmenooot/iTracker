package com.bugztracker.commons.entity.issue;

import com.bugztracker.commons.entity.project.Project;
import com.bugztracker.commons.entity.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Y. Vovk
 * Date: 02.10.15
 * Time: 0:01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Issue implements Serializable {

    @Id
    private String id;

    @NotBlank(message = "Name is required! ")
    @Size(max = 300, message = "Please, shorten the name of issue. Not more than 300 symbols is possible!")
    private String name;

    @Digits(integer = 2, fraction = 1, message = "Version must be a float number from 1 to 10! ")
    @Range(min = 1, max = 10, message = "Version must be a float number from 1 to 10! ")
    @NotNull(message = "Version is required and must be a float number from 1 to 10!")
    private Double version;
    private Date creationDate;
    private Date lastUpdateDate;

    @NotNull(message = "Priority is required! ")
    private Priority priority;

    private Status status;

    private String description;

    @NotNull(message = "Category is required! ")
    private Category category;

    private User creator;

    @NotNull(message = "Assignee is required! ")
    private User assignee;
    private Project project;
    private List<String> attachmentPaths;
    private List<Comment> comments;

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
                .append(name, issue.name)
                .append(creationDate, issue.creationDate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(creationDate)
                .toHashCode();
    }
}
