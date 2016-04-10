package com.bugztracker.persistence.dao;

import com.bugztracker.commons.validators.ICommonsValidator;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Oleh_Osyka
 * Date: 13.02.2016
 * Time: 16:46
 */
public abstract class BaseDao<ENTITY> implements IBaseDao<ENTITY> {

    private final String collectionName;
    private final Class<ENTITY> clazz;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    @Qualifier("commonsPersistenceValidator")
    private ICommonsValidator objectValidator;

    @PostConstruct
    private void initIndexes() {
        createIndex();
    }

    protected BaseDao(String collectionName, Class<ENTITY> clazz) {
        Assert.hasText(collectionName, "collectionName can't be null or empty.");
        Assert.notNull(clazz, "clazz can't be null.");
        this.collectionName = collectionName;
        this.clazz = clazz;
    }

    protected MongoOperations mongo() {
        return mongoOperations;
    }

    protected abstract void createIndex();

    protected void createIndex(Index idx) {
        Assert.notNull(idx, "idx can't be null.");
        Assert.isTrue(idx.getIndexKeys().keySet().size() > 0, "idx.indexKeys are empty.");

        DBCollection collection = this.mongo().getCollection(this.collectionName);
        if (collection == null) {
            collection = mongoOperations.createCollection(this.collectionName);
        }
        collection.createIndex(idx.getIndexKeys());
    }

    @Override
    public ENTITY get(String id) {
//        Assert.notNull(id, "entityID can't be null or empty.");
        return mongoOperations.findById(new ObjectId(id), this.clazz, collectionName);
    }

    @Override
    public List<ENTITY> getAll() {
        return mongo().findAll(this.clazz, collectionName);
    }

    @Override
    public List<ENTITY> findAll(int skip, int limit) {
        Query query = new Query().skip(skip).limit(limit).with(new Sort(Sort.Direction.ASC, "_id"));
        return mongoOperations.find(query, this.clazz, this.collectionName);
    }

    protected List<ENTITY> findAll(Query query) {
        return mongo().find(query, this.clazz, this.collectionName);
    }

    @Override
    public void update(ENTITY object) {
        Assert.notNull(object, "object can't be null.");
        objectValidator.validate(object);
        mongoOperations.save(object, collectionName);
    }

    @Override
    public void add(ENTITY object) {
        Assert.notNull(object, "object can't be null.");
        objectValidator.validate(object);
        mongoOperations.insert(object, this.collectionName);
    }

    @Override
    public void delete(ENTITY object) {
        Assert.notNull(object, "object can't be null.");
        mongoOperations.remove(object, this.collectionName);
    }

    protected WriteResult upsert(Query query, Update update) {
        Assert.notNull(update, "update can't be null.");
        return mongoOperations.upsert(query, update, this.collectionName);
    }

    protected ENTITY findOne(Query query) {
        return mongo().findOne(query, this.clazz, this.collectionName);
    }

    protected <Z extends ENTITY> Z findOne(Query query, Class<Z> clazz) {
        return mongoOperations.findOne(query, clazz, this.collectionName);
    }

}
