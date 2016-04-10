package com.bugztracker.commons.entity.issue;

import com.bugztracker.commons.entity.user.User;
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
public class Comment implements Serializable {

    @Id
    private String id;

    @NotBlank(message = "Comment is required! ")
    @Size(max = 500, message = "Please, shorten the name of issue. Not more than 500 symbols is possible! ")
    private String comment;
    private Date date;
    private User sender;
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Comment comment1 = (Comment) o;

        return new EqualsBuilder()
                .append(comment, comment1.comment)
                .append(date, comment1.date)
                .append(sender, comment1.sender)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(comment)
                .append(date)
                .append(sender)
                .toHashCode();
    }
}
