package bugztracker.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Y. Vovk
 * 02.10.15
 */
@Entity
@Table(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    private int id;
    private String fullName;
    private String password;
    private String email;
    private Timestamp dateExpired;
    private Timestamp dueRegisterDate;
    private boolean isActive;
    private String registrationToken;
    private Set<Project> projects = new HashSet<>();
    private Set<Issue> issuesAss = new HashSet<>();
    private Set<Issue> issuesCr = new HashSet<>();
    private Set<Project> ownedProjects = new HashSet<>();
    private Set<IssueComment> issueComments = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "Full name is required! ")
    @Size(max = 100, message = "Please, shorten your full name to 100 symbols! ")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(nullable = false)
    @NotBlank(message = "Password is required! ")
    @Size(min= 6, message = "Password must be more than 6 symbols length! ")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is required! ")
    @Size(max = 50, message = "Please, use email 50 symbols length! ")
    @Email(message = "Email is not valid! ")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "date_expired")
    public Timestamp getDateExpired() {
        return dateExpired;
    }

    public void setDateExpired(Timestamp dateExpired) {
        this.dateExpired = dateExpired;
    }

    @ManyToMany(mappedBy = "participants")
    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @OneToMany(mappedBy = "assignee")
    public Set<Issue> getIssuesAss() {
        return issuesAss;
    }

    public void setIssuesAss(Set<Issue> issuesAss) {
        this.issuesAss = issuesAss;
    }

    @OneToMany(mappedBy = "userCreator")
    public Set<Issue> getIssuesCr() {
        return issuesCr;
    }

    public void setIssuesCr(Set<Issue> issuesCr) {
        this.issuesCr = issuesCr;
    }

    @OneToMany(mappedBy = "userOwner")
    public Set<Project> getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(Set<Project> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }

    @OneToMany(mappedBy = "sender")
    public Set<IssueComment> getIssueComments() {
        return issueComments;
    }

    public void setIssueComments(Set<IssueComment> issueComments) {
        this.issueComments = issueComments;
    }

    @Column(name = "due_register_date")
    public Timestamp getDueRegisterDate() {
        return dueRegisterDate;
    }

    public void setDueRegisterDate(Timestamp dueRegisterDate) {
        this.dueRegisterDate = dueRegisterDate;
    }

    @Column(name="is_active", nullable = false)
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Column(name = "registration_token", unique = true)
    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .append(id, user.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("fullName", fullName)
                .append("email", email)
                .append("isActive", isActive)
                .append("dueRegisterDate", dueRegisterDate)
                .append("registrationToken", registrationToken)
                .append("dateExpired", dateExpired)
                .toString();
    }
}
