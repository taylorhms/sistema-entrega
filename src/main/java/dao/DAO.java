package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public interface DAO<T> {
    default public EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("sistema-entrega");
        EntityManager entityManager = factory.createEntityManager();
        return entityManager;
    }

    T findById(int id);
    List<T> findAll();
    void insert(T t);
    void update(T t);
    void delete(T t);
    void close();
}
