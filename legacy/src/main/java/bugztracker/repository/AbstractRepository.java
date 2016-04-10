package bugztracker.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Y. Vovk
 * Date: 17.09.15
 * Time: 23:50
 */
@Repository
public abstract class AbstractRepository<T> implements IRepository<T> {

    @Autowired
    protected SessionFactory sessionFactory;

    private Class<T> type;

    public AbstractRepository(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get(int id) {
        return (T) sessionFactory.getCurrentSession().get(type, id);
    }

    @Override
    public List<T> getAll() {
        return (List<T>) sessionFactory.getCurrentSession().createCriteria(type).list();
    }

    @Override
    public void add(T entity) {
        sessionFactory.getCurrentSession().save(entity);
    }

    @Override
    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    public void update(T entity) {
        sessionFactory.getCurrentSession().update(entity);
    }

}
