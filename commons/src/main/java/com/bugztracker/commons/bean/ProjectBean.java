package com.bugztracker.commons.bean;

import java.util.List;

/**
 * Author: Yuliia Vovk
 * Date: 04.03.16
 * Time: 12:21
 */
public class ProjectBean {

    private String id;
    private String name;
    private String description;
    private List<String> userIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
