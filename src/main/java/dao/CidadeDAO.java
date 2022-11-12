package dao;

import entidades.Cidade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public class CidadeDAO implements DAO<Cidade> {
    EntityManager entityManager = getEntityManager();

    @Override
    public Cidade findById(int id) {
        return entityManager.find(Cidade.class, id);
    }

    @Override
    public List<Cidade> findAll() {
    	Query query = entityManager.createQuery("select c from Cidade c", Cidade.class);
    	return query.getResultList();
    }

    @Override
    public void insert(Cidade t) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(t);
        transaction.commit();
    }

    @Override
    public void update(Cidade t) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(t);
        transaction.commit();
    }

    @Override
    public void delete(Cidade t) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Cidade c = entityManager.find(Cidade.class, t.getId());
        entityManager.remove(c);
        transaction.commit();
    }

	@Override
	public void close() {
		entityManager.close();
	}

}
