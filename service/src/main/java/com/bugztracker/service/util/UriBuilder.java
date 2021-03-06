package com.bugztracker.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public final class UriBuilder {

    private static final String SLASH = File.separator;

    @Value("${service.repository.path}")
    private String rootPath;

    private UriBuilder() {
    }

    public String buildPathForIssueFolder(String issueId) {
        return build().
                append(SLASH).
                append(issueId).
                append(SLASH).
                toString();
    }

    public String buildPathForAttachment(String issueId, String filename) {
        return build().
                append(SLASH).
                append(issueId).
                append(SLASH).
                append(filename.replaceAll(" ", "_")).
                toString();
    }

    private StringBuilder build() {
        return new StringBuilder(System.getProperty("user.home")).
                append(rootPath);
    }

}
