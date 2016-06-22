package com.bugztracker.persistence.utils;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public final class DbUtils {

    private DbUtils() {
    }

    public static List<ObjectId> convertIds(List<String> ids) {
        List<ObjectId> result = new ArrayList<>();
        for (String id : ids) {
            result.add(new ObjectId(id));
        }
        return result;
    }
}
