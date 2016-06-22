package com.bugztracker.service.impl;

import com.bugztracker.commons.bean.MailBean;
import com.bugztracker.commons.entity.user.User;
import com.bugztracker.service.IEmailService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class EmailService implements IEmailService {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${service.mail.sender}")
    private String mailSendFrom;

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    private void sendEmail(MailBean mailBean, VelocityContext velocityContext) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailBean.getMailFrom());
        message.setTo(mailBean.getMailTo());
        message.setSubject(mailBean.getMailSubject());

        Template template = velocityEngine.getTemplate("./templates/" + mailBean.getTemplateName());

        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);
        message.setText(stringWriter.toString());

        mailSender.send(message);
    }

    @Override
    public void sendRegisterEmail(User sendTo) {
        MailBean mailBean = new MailBean();
        mailBean.setMailFrom(mailSendFrom);
        mailBean.setMailTo(sendTo.getEmail());
        mailBean.setMailSubject("Activation link for iTracker");
        mailBean.setTemplateName("confirmation-template.vm");

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("fullName", sendTo.getFullName());

        try {
            velocityContext.put("link",  InetAddress.getLocalHost().getHostAddress() + ":8082/api/auth/activate/" + sendTo.getRegistrationToken());
        } catch (UnknownHostException e) {
            LOG.error("Can't get host address {}", e);
        }

        sendEmail(mailBean, velocityContext);
    }
}
