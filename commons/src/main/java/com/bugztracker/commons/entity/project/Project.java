package com.bugztracker.commons.entity.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Yuliia Vovk
 * Date: 04.11.15
 * Time: 10:56
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project implements Serializable {

    @Id
    private String id;

    @NotBlank(message = "Name is required! ")
    @Size(max = 300, message = "Please, shorten the name of project. Not more than 300 symbols is possible! ")
    private String name;
    private Date date;
    private String description;
    private boolean isArchived;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        return new EqualsBuilder()
                .append(id, project.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
