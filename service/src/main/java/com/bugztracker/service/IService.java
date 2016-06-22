package com.bugztracker.service;

import java.util.Optional;

public interface IService<ENTITY> {

    Optional<ENTITY> get(String id);

}
