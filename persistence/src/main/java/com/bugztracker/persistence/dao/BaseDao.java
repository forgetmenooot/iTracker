package com.bugztracker.persistence.dao;

import com.mongodb.DBCollection;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

public abstract class BaseDao<ENTITY> implements IBaseDao<ENTITY> {

    private final String collectionName;
    private final Class<ENTITY> clazz;

    @Autowired
    protected MongoOperations mongoOperations;

    @PostConstruct
    private void initIndexes() {
        createIndex();
    }

    protected BaseDao(String collectionName, Class<ENTITY> clazz) {
        this.collectionName = collectionName;
        this.clazz = clazz;
    }

    protected abstract void createIndex();

    protected void createIndex(Index idx) {
        Optional<DBCollection> collectionOptional = Optional.ofNullable(mongoOperations.getCollection(collectionName));
        if (!collectionOptional.isPresent()) {
            mongoOperations.createCollection(collectionName);
        }
        collectionOptional.get().createIndex(idx.getIndexKeys());
    }

    @Override
    public ENTITY get(String id) {
        return mongoOperations.findById(new ObjectId(id), clazz, collectionName);
    }

    @Override
    public List<ENTITY> getAll() {
        return mongoOperations.findAll(clazz, collectionName);
    }

    protected List<ENTITY> findAll(Query query) {
        return mongoOperations.find(query, clazz, collectionName);
    }

    @Override
    public void update(ENTITY object) {
        mongoOperations.save(object, collectionName);
    }

    @Override
    public void add(ENTITY object) {
        mongoOperations.insert(object, collectionName);
    }

    @Override
    public void delete(ENTITY object) {
        mongoOperations.remove(object, collectionName);
    }

    protected ENTITY findOne(Query query) {
        return mongoOperations.findOne(query, clazz, collectionName);
    }

}
