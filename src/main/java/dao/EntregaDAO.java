package dao;

import entidades.Entrega;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public class EntregaDAO implements DAO<Entrega> {
    EntityManager entityManager = getEntityManager();

    @Override
    public Entrega findById(int id) {
        return entityManager.find(Entrega.class, id);
    }

    @Override
    public List<Entrega> findAll() {
    	Query query = entityManager.createQuery("select e from Entrega e", Entrega.class);
    	return query.getResultList();
    }

    @Override
    public void insert(Entrega t) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(t);
        transaction.commit();
    }

    @Override
    public void update(Entrega t) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(t);
        transaction.commit();
    }

    @Override
    public void delete(Entrega t) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Entrega e = entityManager.find(Entrega.class, t.getId());
        entityManager.remove(e);
        transaction.commit();
    }

	@Override
	public void close() {
		entityManager.close();
	}

}
