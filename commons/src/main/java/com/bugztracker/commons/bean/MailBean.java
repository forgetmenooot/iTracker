package com.bugztracker.commons.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.sql.Timestamp;

public class MailBean {

    private String mailFrom;
    private String mailTo;
    private String mailCc;
    private String mailBcc;
    private String mailSubject;
    private String mailContent;
    private String templateName;
    private String contentType;

    public MailBean() {
        contentType = "text/html";
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMailBcc() {
        return mailBcc;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setMailBcc(String mailBcc) {
        this.mailBcc = mailBcc;
    }

    public String getMailCc() {
        return mailCc;
    }

    public void setMailCc(String mailCc) {
        this.mailCc = mailCc;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public Timestamp getMailSendDate() {
        return new Timestamp(DateTime.now().getMillis());
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("MailBean From:- ").append(getMailFrom())
                .append("MailBean To:- ").append(getMailTo())
                .append("MailBean Cc:- ").append(getMailCc())
                .append("MailBean Bcc:- ").append(getMailBcc())
                .append("MailBean Subject:- ").append(getMailSubject())
                .append("MailBean Send Date:- ").append(getMailSendDate())
                .append("MailBean Content:- ").append(getMailContent())
                .toString();
    }

}
