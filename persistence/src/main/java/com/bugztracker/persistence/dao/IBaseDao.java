package com.bugztracker.persistence.dao;

import java.util.List;

public interface IBaseDao<ENTITY> {

    List<ENTITY> getAll();
    ENTITY get(String id);
    void add(ENTITY entity);
    void delete(ENTITY entity);
    void update(ENTITY entity);

}
